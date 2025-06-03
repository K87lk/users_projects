package org.example.users_projects.mapper;

import org.example.users_projects.dto.UserRequestDTO;
import org.example.users_projects.dto.UserResponseDTO;
import org.example.users_projects.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {SubscriptionMapper.class})
public abstract class UserMapper {

    protected PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    public abstract User toEntity(UserRequestDTO userRequestDTO);

    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true) // сохраняем старое значение
    @Mapping(target = "id", ignore = true)        // не трогаем id
    public abstract void updateEntity(@MappingTarget User user, UserRequestDTO userRequestDTO);

    public abstract UserResponseDTO toDTO(User user);

    @Named("encodePassword")
    protected String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    @Named("generateCreatedAt")
    protected LocalDateTime generateCreatedAt(UserRequestDTO dto) {
        return LocalDateTime.now();
    }
    @Named("generateUpdatedAt")
    protected LocalDateTime generateUpdatedAt(UserRequestDTO dto) {
        return LocalDateTime.now();
    }
}
