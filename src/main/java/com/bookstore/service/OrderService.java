package com.bookstore.service;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(CreateOrderRequestDto createOrderRequestDto);

    List<OrderResponseDto> findOrderHistory();

    OrderResponseDto updateOrderStatus(
            Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    List<OrderItemDto> findAllFromOrder(Long orderId);

    OrderItemDto findItemFromOrder(Long orderId, Long itemId);
}
