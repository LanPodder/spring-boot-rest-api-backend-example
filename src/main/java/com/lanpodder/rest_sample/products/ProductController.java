package com.lanpodder.rest_sample.products;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService){
    this.productService = productService;
  }

  @GetMapping("")
  public List<ProductDTO> getProducts() {
      return productService.getAllProducts();
  }

  @PostMapping("")
  public ProductDTO createProduct(@Valid @RequestBody ProductDTO product) {
      return productService.createProduct(product);
  }
}
