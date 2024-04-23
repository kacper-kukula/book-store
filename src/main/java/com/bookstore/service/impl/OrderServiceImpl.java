package com.bookstore.service.impl;

import com.bookstore.dto.order.CreateOrderRequestDto;
import com.bookstore.dto.order.OrderItemDto;
import com.bookstore.dto.order.OrderResponseDto;
import com.bookstore.dto.order.UpdateOrderStatusRequestDto;
import com.bookstore.exception.EntityNotFoundException;
import com.bookstore.mapper.OrderItemMapper;
import com.bookstore.mapper.OrderMapper;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import com.bookstore.repository.order.OrderRepository;
import com.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.bookstore.service.OrderService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_NOT_FOUND_ERR = "Order not found with ID: ";

    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(User user, CreateOrderRequestDto createOrderRequestDto) {
        ShoppingCart shoppingCart = getCartIfValid(user);
        String shippingAddress = createOrderRequestDto.shippingAddress();
        BigDecimal total = calculateTotalPrice(shoppingCart.getCartItems());

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus(Order.Status.PENDING);
        newOrder.setTotal(total);
        newOrder.setShippingAddress(shippingAddress);
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(item -> orderItemMapper.toOrderItem(item, newOrder))
                .collect(Collectors.toSet());
        newOrder.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(newOrder);
        shoppingCartRepository.delete(shoppingCart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findOrderHistory(User user, Pageable pageable) {
        return orderRepository.findAllByUser(user, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(
            Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto) {
        Order.Status status = updateOrderStatusRequestDto.status();

        Order order = orderRepository.getReferenceById(orderId);

        boolean isValidStatus = Arrays.asList(Order.Status.values()).contains(status);

        if (!isValidStatus) {
            throw new IllegalArgumentException("Order status doesn't exists: " + status);
        }

        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> findAllFromOrder(Long orderId, Pageable pageable) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(ORDER_NOT_FOUND_ERR + orderId));

        List<OrderItemDto> allOrderItems = order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        int endItem = Math.min(startItem + pageSize, allOrderItems.size());

        return allOrderItems.subList(startItem, endItem);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDto findItemFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(ORDER_NOT_FOUND_ERR + orderId));

        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() ->
                        new EntityNotFoundException("Couldn't find item with ID: " + itemId));

        return orderItemMapper.toDto(orderItem);
    }

    private ShoppingCart getCartIfValid(User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseThrow(() ->
                        new EntityNotFoundException("Please grab a shopping cart first."));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Please add some items to the shopping cart first.");
        }

        return shoppingCart;
    }

    private BigDecimal calculateTotalPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getBook()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
