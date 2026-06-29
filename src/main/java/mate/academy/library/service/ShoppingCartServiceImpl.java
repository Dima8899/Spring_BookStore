package mate.academy.library.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dao.BookRepository;
import mate.academy.library.dao.CartItemRepository;
import mate.academy.library.dao.ShoppingCartRepository;
import mate.academy.library.dto.cart.CartItemRequestDto;
import mate.academy.library.dto.cart.ShoppingCartResponseDto;
import mate.academy.library.dto.cart.UpdateCartItemRequestDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.CartItemMapper;
import mate.academy.library.mapper.ShoppingCartMapper;
import mate.academy.library.model.Book;
import mate.academy.library.model.CartItem;
import mate.academy.library.model.ShoppingCart;
import mate.academy.library.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public ShoppingCartResponseDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + userId));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBookToCart(Long userId, CartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user: " + userId));

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found: " + requestDto.getBookId()));

        Optional<CartItem> existingItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(
                    existingItem.get().getQuantity() + requestDto.getQuantity()
            );
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setBook(book);
            cartItem.setQuantity(requestDto.getQuantity());
            cartItem.setShoppingCart(shoppingCart);
            shoppingCart.getCartItems().add(cartItem);
        }

        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItemQuantity(Long cartItemId, Long userId,
                                                          UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item by id: "
                        + cartItemId + "not found"));

        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void removeCartItem(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + userId));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item by id: "
                        + cartItemId + "not found"));

        cartItemRepository.delete(cartItem);

    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
