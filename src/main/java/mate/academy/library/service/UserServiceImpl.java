package mate.academy.library.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dao.RoleRepository;
import mate.academy.library.dao.UserRepository;
import mate.academy.library.dto.UserRegistrationRequestDto;
import mate.academy.library.dto.UserResponseDto;
import mate.academy.library.exception.RegistrationException;
import mate.academy.library.mapper.UserMapper;
import mate.academy.library.model.Role;
import mate.academy.library.model.RoleName;
import mate.academy.library.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException(
                    "Email already registered: " + request.getEmail()
            );
        }

        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.USER)
                        .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
