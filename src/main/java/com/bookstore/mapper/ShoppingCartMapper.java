package com.bookstore.mapper;

import com.bookstore.config.MapperConfig;
import com.bookstore.dto.shoppingcart.CartItemResponseDto;
import com.bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.bookstore.model.CartItem;
import com.bookstore.model.ShoppingCart;
import com.bookstore.model.User;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    default Long map(User user) {
        return user.getId();
    }

    Set<CartItemResponseDto> map(Set<CartItem> cartItems);
}
