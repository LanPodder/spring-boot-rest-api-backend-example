package com.lanpodder.rest_sample.orders;

import java.util.List;

import com.lanpodder.rest_sample.products.Product;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String description;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<Product> products;

  public void addProduct(Product p) {
    products.add(p);
    p.setOrder(this);
  }
}
