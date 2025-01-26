package com.jeanbarcellos.project111.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.jeanbarcellos.project111.dto.UserRequest;
import com.jeanbarcellos.project111.dto.UserResponse;
import com.jeanbarcellos.project111.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public User toEntity(UserRequest request) {
        return new User(
                request.getName(),
                request.getEmail(),
                request.getPassword());
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public List<UserResponse> toResponseList(List<User> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    public User copy(User destination, UserRequest source) {
        destination.setName(source.getName());
        destination.setEmail(source.getEmail());
        destination.setPassword(source.getPassword());
        return destination;
    }

}
