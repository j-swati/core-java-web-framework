package com.framework.app.service;

import com.framework.annotations.Inject;
import com.framework.annotations.Service;
import com.framework.app.dto.CreateUserRequest;
import com.framework.app.dto.UserResponseDto;
import com.framework.app.model.Role;
import com.framework.app.model.User;
import com.framework.app.repository.UserRepository;
import com.framework.app.validation.UserValidator;
import com.framework.exception.EntityNotFoundException;

import java.util.List;

@Service
public class UserService {

    @Inject
    private UserRepository userRepository;

    public UserResponseDto createUser(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("CreateUserRequest cannot be null");
        }
        return createUser(request.name(), request.email(), request.role());
    }

    public UserResponseDto createUser(String name, String email, Role role) {
        UserValidator.validate(name, email);
        User user = new User(null, name, email, role != null ? role : Role.USER);
        User saved = userRepository.save(user);
        return mapToDto(saved);
    }

    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with ID: " + id));
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null
        );
    }
}


// .map(this::mapToDto) -- If a User exists, convert that User into a UserResponseDto using mapToDto()  -- method reference

// Service layer -- business logic.
// @Inject -- inject UserRepository.
// Service uses the repository to save/find users and maps entities to DTOs.
// Optional + map() handles successful results, while orElseThrow() handles missing users.
