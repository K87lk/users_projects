package org.example.users_projects.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.users_projects.dto.UserRequestDTO;
import org.example.users_projects.dto.UserResponseDTO;
import org.example.users_projects.mapper.UserMapper;
import org.example.users_projects.model.User;
import org.example.users_projects.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            log.error("Username %s is already taken : {}", userRequestDTO.getUsername());
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            log.error("Email %s is already in use : {}", userRequestDTO.getEmail());
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = userMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);

        log.info("User created: {}", savedUser);
        return userMapper.toDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        if (userRequestDTO.getUsername() != null) {
            existingUser.setUsername(userRequestDTO.getUsername());
        }
        if (userRequestDTO.getEmail() != null) {
            existingUser.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getPassword() != null) {
            existingUser.setPassword((userRequestDTO.getPassword()));
        }

        userMapper.updateEntity(existingUser, userRequestDTO);

        User updatedUser = userRepository.save(existingUser);

        log.info("User updated: {}", updatedUser);
        return userMapper.toDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        log.info("User deleted with id: {}", id);
        userRepository.deleteById(id);
    }
}