package mate.academy.library.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.library.model.Order;

@Data
public class UpdateOrderStatusRequestDto {

    @NotNull
    private Order.Status status;
}
