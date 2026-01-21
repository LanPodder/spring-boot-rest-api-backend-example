FROM gradle:jdk21 AS builder

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts settings.gradle.kts ./

RUN gradle dependencies --no-daemon

COPY src src
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]