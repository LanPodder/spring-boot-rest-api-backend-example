# Rest API backend Spring Boot sample
A sample backend API in spring boot including 
* postgres
* spring security + keycloak
* unit tests
* minio for s3 bucket
* openapi docs

so dont lose my mind every time i setup a new backend project.  
Includes apidocs.html for rendering openapi docs in scalar, which i think look pretty.  
Would love to use Kotlin for source code but vscode extension is too unstable for now (yes i use vscode, come at me intellij fanboys).

WIP

# Contents
[Getting Started](#getting-started)  
[Guides](#foo)  
&nbsp;&nbsp;&nbsp;&nbsp;[Database](#database)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[TLDR](#database-tldr)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[Non TLDR](#database-non-tldr)  
&nbsp;&nbsp;&nbsp;&nbsp;[Security](#security)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[TLDR](#security-tldr)  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[Non TLDR](#security-non-tldr)  
&nbsp;&nbsp;&nbsp;&nbsp;[Tests](#tests)  
[Keycloak](#keycloak)  

# Getting started
Requires  
* Java 21+  
* Docker or Podman  

For local development run `./gradlew bootRun`, for building a jar locally `./gradlew bootJar`.  
Run Services (Keycloak, postgres, etc.) with `docker compose up` or `podman compose up`.  
Run individual services with `<docker|pdoman> compose up <servicename>`.  
Rebuild without nuking containers with `<docker|podman> compose up --build`.  
Run tests with `./gradlew test`, generates coverage report with jacoco  
While the api is running, open the `apidocs.html` static html file in a browser to view apidocs
# Guides
Some documentation for my own future sanity
## Database
### TLDR <a id="database-tldr"></a>
repository for SQL logic methods
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
  
}
```
"Regular" Model as JPA Entity, including @OneToMany etc.
```java
@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false)
  private String name;
  
  @Column(nullable = false)
  private String description;
}
```
DTO for free use in Services and Controllers
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
  @NonNull
  private String name;
  @NonNull
  private String description;
}
```
### Non TLDR <a id="database-non-tldr"></a>  
todo

## Security
### TLDR <a id="security-tldr"></a>  
todo

### Non TLDR <a id="security-non-tldr"></a>  
For security we need those two lines in application.properties:
```
spring.security.oauth2.resourceserver.jwt.issuer-uri=${jwt_issuer_uri}
spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${jwt_issuer_uri}/protocol/openid-connect/certs
```
jwt issuer being keycloak in our case, we set the `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI` enironment variable to our keycloak url in our `docker-compose.yml`.  
Then we need a configuration class to include a SecurityFilterChain bean, located in src/.../configuration
```java
@Configuration
public class WebSecurityConfig{
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http){
    return http
    ...
  }
}
```
then using this http parameter we can add configurations.
we add `.requestMatchers("/openapi/**").permitAll()` so we can access our openapi docs without needing to authenticate

## Tests
Apparently there is a huge difference between `@SpringBootTest`, `@WebMvcTest` and no test class annotation but just `@Test` annotations on methods. Apparently `@SpringBootTest` is used to run integration tests and not required for unit tests. Turns out `@SpringBootTest` causes the entire context to be created, meaning every bean, service and whatnot gets created. This caused issues with Security as we would need to mock security related beans in every test. The solution was to add 
```java
@MockitoBean
JwtDecoder jwtDecoder;
```
to the main `@SpringBootTest` and avoid using that for the majority of tests.
It turns out when testing services we dont even need class annotations, we can simply instantiate the service with `new WhateverService()` or if we need dependencies such as a repository, we use following mock annotations:
```java
@Mock
ProductMapper productMapper;
@Mock
ProductRepository productRepository;

@InjectMocks
ProductService productService;
```

We test controllers using `@WebMvcTest(WhateverController.class)`, add a
```java
@Autowired
MockMvc mockMvc;
```
for giving us utilities to send a mock request and test against that.
Additionally we need these two static imports:
```
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
```
to allow us to use `post(), get(), put(), delete()` as well as `status, jsonPath` and whatever else there is for sending requests and validating responses.  
Because we are using security, we annotate every controllertest with `@WithMockUser(username = "whatever", roles = "whatever")` in order to be authenticated (unless we test for unauthenticated or unauthorized)

# Keycloak
Running a simple keycloak docker container from docker compose causes a ton of problems somehow. We know docker containers communicate via their containername as hostname. So if we call `localhost` from inside our api container, we would be unable to reach our keycloak container as both have different `localhost`s. Therefore one would think we need to set the issuer uri to `keycloak:8080`, however this causes us to be unable to reach our keycloak admin console at `localhost:8080` in our browsers (at least on windows, we get redirected to `keycloak:8080` in our browser which is invalid/unreachable).  
Therefore we need to set the KC_HOSTNAME to localhost which causes the issuer in our jwt tokens to be set to `localhost:8080`. So we set our issuer uri to `localhost` in our api container. However when our api container tries to send a request to `localhost:8080` it does not reach our keycloak container -.-  
The solution is to add
```yaml
    extra_hosts:
      - "localhost:host-gateway"
```
to our api container and be done with it. Thats not something we should keep in any production environment.  
A possible different, cleaner solution couldve been adding `KC_HOSTNAME_ADMIN: http://localhost:8080` next to `KC_HOSTNAME: http://keycloak:8080` which is supposed to assign a different hostname for our admin console, however when opening `localhost:8080` it somehow tries to render this iframe:
```
<iframe src="http://keycloak:8080/realms/master/protocol/openid-connect/3p-cookies/step1.html" ...
```
So yeah unless that gets fixed this approach is unusable.