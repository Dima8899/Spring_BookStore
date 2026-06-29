package mate.academy.library.service;

import mate.academy.library.dto.order.CreateOrderRequestDto;
import mate.academy.library.dto.order.OrderItemResponseDto;
import mate.academy.library.dto.order.OrderResponseDto;
import mate.academy.library.dto.order.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto createOrder(Long userId, CreateOrderRequestDto requestDto);

    Page<OrderResponseDto> getOrderHistory(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);

    Page<OrderItemResponseDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
