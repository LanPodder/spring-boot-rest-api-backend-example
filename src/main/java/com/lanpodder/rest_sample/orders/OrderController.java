package com.lanpodder.rest_sample.orders;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("")
  public List<OrderDTO> getAllOrders() {
    return orderService.getAllOrders();
  }

  @PostMapping("")
  public OrderDTO createOrder(@Valid @RequestBody CreateOrderDTO createOrder) {
    return orderService.createOrder(createOrder);
  }

  @PostMapping("/{orderId}/assign")
  public OrderDTO assignProduct(@PathVariable Long orderId, @Valid @RequestBody Long productId) {
    return orderService.assignProduct(orderId, productId);
  }
}
