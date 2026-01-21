package com.lanpodder.rest_sample.products;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
  @Mock
  ProductMapper productMapper;
  @Mock
  ProductRepository productRepository;
  
  @InjectMocks
  ProductService productService;

  @Test
  void shouldGetAllProducts(){
    List<Product> products = List.of(Product.builder().id(1l).name("prod1").description("its a prod 1").build());
    List<ProductDTO> productsDtos = List.of(ProductDTO.builder().name("prod1").description("its a prod 1").build());
    when(productRepository.findAll()).thenReturn(products);
    when(productMapper.allToDTO(products)).thenReturn(productsDtos);

    List<ProductDTO> result = productService.getAllProducts();

    assertEquals(productsDtos, result);
  }
}
