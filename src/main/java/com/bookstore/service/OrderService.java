package com.bookstore.service;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto placeOrder(User user, CreateOrderRequestDto createOrderRequestDto);

    List<OrderResponseDto> findOrderHistory(User user, Pageable pageable);

    OrderResponseDto updateOrderStatus(
            Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    List<OrderItemDto> findAllFromOrder(Long orderId, Pageable pageable);

    OrderItemDto findItemFromOrder(Long orderId, Long itemId);
}
