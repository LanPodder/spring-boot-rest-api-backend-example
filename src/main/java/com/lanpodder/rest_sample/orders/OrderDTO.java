package com.lanpodder.rest_sample.orders;

import java.util.List;

import com.lanpodder.rest_sample.products.ProductDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDTO {
  private Long id;
  private String description;
  private List<ProductDTO> products;
}
