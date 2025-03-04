package com.example.shoppingCart.service.order;

import com.example.shoppingCart.dto.OrderDto;
import com.example.shoppingCart.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder (Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
