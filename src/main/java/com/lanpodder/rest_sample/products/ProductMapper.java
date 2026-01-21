package com.lanpodder.rest_sample.products;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  ProductDTO toDTO(Product p);
  Product toEntity(ProductDTO p);

  List<ProductDTO> allToDTO(List<Product> products);
  List<Product> allToEntity(List<ProductDTO> products);
}
