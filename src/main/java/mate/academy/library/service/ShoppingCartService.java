package mate.academy.library.service;

import mate.academy.library.dto.cart.CartItemRequestDto;
import mate.academy.library.dto.cart.ShoppingCartResponseDto;
import mate.academy.library.dto.cart.UpdateCartItemRequestDto;
import mate.academy.library.model.User;

public interface ShoppingCartService {

    ShoppingCartResponseDto getShoppingCart(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId, CartItemRequestDto requestDto);

    ShoppingCartResponseDto updateCartItemQuantity(Long cartItemId,
                                                   UpdateCartItemRequestDto requestDto);

    void removeCartItem(Long cartItemId);

    void createShoppingCart(User user);
}
