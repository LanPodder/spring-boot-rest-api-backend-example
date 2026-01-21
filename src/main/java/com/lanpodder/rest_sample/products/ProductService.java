package com.lanpodder.rest_sample.products;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  public ProductService(ProductRepository productRepository, ProductMapper productMapper){
    this.productRepository = productRepository;
    this.productMapper = productMapper;
  }

  public List<ProductDTO> getAllProducts(){
    return productMapper.allToDTO(productRepository.findAll());
  }
}
