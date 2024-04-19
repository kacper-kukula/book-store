package com.bookstore.service;

import com.bookstore.dto.order.OrderItemResponseDto;
import com.bookstore.dto.order.OrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.OrderStatusRequestDto;
import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto);

    List<OrderResponseDto> findOrderHistory();

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatusRequestDto orderStatusRequestDto);

    List<OrderItemResponseDto> findAllFromOrder(Long orderId);

    OrderItemResponseDto findItemFromOrder(Long orderId, Long itemId);
}
