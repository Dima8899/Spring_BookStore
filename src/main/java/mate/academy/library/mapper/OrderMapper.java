package mate.academy.library.mapper;

import mate.academy.library.dto.order.OrderResponseDto;
import mate.academy.library.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "status", source = "status")
    OrderResponseDto toDto(Order order);
}
