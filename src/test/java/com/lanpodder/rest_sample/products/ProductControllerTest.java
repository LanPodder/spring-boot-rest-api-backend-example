package com.lanpodder.rest_sample.products;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tools.jackson.databind.json.JsonMapper;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
  @Autowired
  MockMvc mockMvc;

  private JsonMapper jsonMapper = JsonMapper.builder().build();

  @MockitoBean
  ProductService productService;

  @Test
  @WithMockUser(username = "admin", roles = "admin")
  void shouldGetAllProducts() throws Exception {
    List<ProductDTO> productDTOs = List.of(ProductDTO.builder().name("prod 1").description("its a prod 1").build());
    when(productService.getAllProducts()).thenReturn(productDTOs);
    mockMvc.perform(get("/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("prod 1"));
  }

  @Test
  @WithMockUser(username = "admin", roles = "admin")
  void shouldCreateProduct() throws Exception {
    ProductDTO productDTO = ProductDTO.builder().name("prod 1").description("its a prod 1").build();
    mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonMapper.writeValueAsString(productDTO))
        .with(csrf()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = "admin")
  void shouldReturn400Invalid() throws Exception {
    mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}")
        .with(csrf()))
        .andExpect(status().isBadRequest());
  }
}
