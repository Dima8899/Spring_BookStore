package mate.academy.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.UserRegistrationRequestDto;
import mate.academy.library.dto.UserResponseDto;
import mate.academy.library.exception.RegistrationException;
import mate.academy.library.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;

    @PostMapping("/registration")
    public UserResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
