package mate.academy.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.cart.CartItemRequestDto;
import mate.academy.library.dto.cart.ShoppingCartResponseDto;
import mate.academy.library.dto.cart.UpdateCartItemRequestDto;
import mate.academy.library.model.User;
import mate.academy.library.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Category management")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shopping cart")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add book to shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto addBookToCart(
            Authentication authentication,
            @RequestBody @Valid CartItemRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addBookToCart(user.getId(), requestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity of a books in the shopping cart")
    public ShoppingCartResponseDto updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto requestDto,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateCartItemQuantity(cartItemId, user.getId(), requestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove book from shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCartItem(@PathVariable Long cartItemId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.removeCartItem(cartItemId, user.getId());
    }
}
