package mate.academy.library.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {

    @NotBlank(message = "Title cannot be null")
    private String title;

    @NotBlank(message = "Author cannot be null")
    private String author;

    @NotBlank(message = "ISBN cannot be null")
    private String isbn;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be grater than 0")
    private BigDecimal price;
    private String description;
    private String coverImage;
}
