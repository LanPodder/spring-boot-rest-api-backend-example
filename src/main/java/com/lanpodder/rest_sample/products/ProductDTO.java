package com.lanpodder.rest_sample.products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
