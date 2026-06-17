package mate.academy.library.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.UserLoginRequestDto;
import mate.academy.library.dto.UserLoginResponseDto;
import mate.academy.library.dto.UserRegistrationRequestDto;
import mate.academy.library.dto.UserResponseDto;
import mate.academy.library.exception.RegistrationException;
import mate.academy.library.security.AuthenticationService;
import mate.academy.library.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "User registration and login")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Login", description = "Login for user")
    @PostMapping("/login")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return authenticationService.authenticate(userLoginRequestDto);
    }

    @Operation(summary = "Registration", description = "Registers the user")
    @PostMapping("/registration")
    public UserResponseDto register(
            @Valid @RequestBody UserRegistrationRequestDto request)
            throws RegistrationException {
        return userService.register(request);
    }
}
