package mate.academy.library.service;

import mate.academy.library.dto.UserRegistrationRequestDto;
import mate.academy.library.dto.UserResponseDto;
import mate.academy.library.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException;
}
