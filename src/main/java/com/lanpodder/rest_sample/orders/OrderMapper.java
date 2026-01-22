package com.lanpodder.rest_sample.orders;

import java.util.List;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderDTO toDto(Order order);
  Order toEntity(OrderDTO orderDTO);
  Order toEntity(CreateOrderDTO createOrderDTO);

  List<OrderDTO> allToDto(List<Order> orders);
  List<Order> allToEntity(List<OrderDTO> orderDTOs);
}
