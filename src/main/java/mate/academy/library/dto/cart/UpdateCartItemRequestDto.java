package mate.academy.library.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {

    @Positive
    private int quantity;
}
