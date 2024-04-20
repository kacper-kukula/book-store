package com.bookstore.controller;

import com.bookstore.dto.order.OrderItemResponseDto;
import com.bookstore.dto.order.OrderRequestDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.OrderStatusRequestDto;
import com.bookstore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User's orders",
        description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Place an order",
            description = "Places an order using shopping cart and shipping address input")
    public OrderResponseDto placeOrder(
            @RequestBody @Valid OrderRequestDto orderRequestDto) {
        return orderService.placeOrder(orderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Retrieve order history",
            description = "Retrieves all orders belonging to currently logged in user")
    public List<OrderResponseDto> findOrderHistory() {
        return orderService.findOrderHistory();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Updates the status of particular order according to status ENUM")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderStatusRequestDto orderStatusRequestDto) {
        return orderService.updateOrderStatus(orderId, orderStatusRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Retrieve items from an order",
            description = "Retrieves all items from an order with particular id")
    public List<OrderItemResponseDto> findAllFromOrder(@PathVariable Long orderId) {
        return orderService.findAllFromOrder(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Retrieve single item from an order",
            description = "Retrieves single item by ID from an order by ID")
    public OrderItemResponseDto findItemFromOrder(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.findItemFromOrder(orderId, itemId);
    }
}