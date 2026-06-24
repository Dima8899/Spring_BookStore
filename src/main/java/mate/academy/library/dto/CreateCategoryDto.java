package mate.academy.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryDto {

    @NotBlank
    private String name;
    private String description;

}
