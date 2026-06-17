package mate.academy.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank(message = "Email cannot be blank")
        @Size(min = 8, max = 20)
        @Email
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, max = 20)
        String password
) {
}
