package com.lanpodder.rest_sample.orders;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lanpodder.rest_sample.products.Product;
import com.lanpodder.rest_sample.products.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {
  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;

  private final ProductRepository productRepository;

  public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
    this.productRepository = productRepository;
  }

  public List<OrderDTO> getAllOrders() {
    return orderMapper.allToDto(orderRepository.findAll());
  }

  public OrderDTO createOrder(CreateOrderDTO createOrder) {
    return orderMapper.toDto(orderRepository.save(orderMapper.toEntity(createOrder)));
  }

  public OrderDTO assignProduct(Long orderId, Long productId) {
    Order order = orderRepository.findById(orderId).orElseThrow();
    Product product = productRepository.findById(productId).orElseThrow();
    order.addProduct(product);
    return orderMapper.toDto(orderRepository.save(order));
  }
}
