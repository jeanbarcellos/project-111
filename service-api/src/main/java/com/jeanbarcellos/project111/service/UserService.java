package com.jeanbarcellos.project111.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jeanbarcellos.core.constants.MessageConstants;
import com.jeanbarcellos.core.exception.NotFoundException;
import com.jeanbarcellos.core.exception.ValidationException;
import com.jeanbarcellos.project111.dto.UserResponse;
import com.jeanbarcellos.project111.entity.User;
import com.jeanbarcellos.project111.mapper.UserMapper;
import com.jeanbarcellos.project111.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String MSG_ERROR_USER_NOT_FOUND = "There is no user for the ID '%s' provided.";
    public static final String MSG_ERROR_USER_NOT_FOUND_BY_EMAIL = "There is no user for the email '%s' provided.";
    public static final String MSG_ERROR_USER_PERSONAL_NUMBER_EXISTS = "User with CPF '%s' already registered.";
    public static final String MSG_ERROR_USER_EMAIL_EXISTS = "User with email '%s' already registered.";

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<UserResponse> getAll() {
        return this.userMapper.toResponseList(this.userRepository.findAll());
    }

    public UserResponse getById(UUID id) {
        return this.userMapper.toResponse(this.findByIdOrThrow(id));
    }

    public UUID getIdByEmail(String email) {
        return this.userRepository.findIdByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format(MSG_ERROR_USER_NOT_FOUND_BY_EMAIL, email)));
    }

    private User findByIdOrThrow(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(MSG_ERROR_USER_NOT_FOUND, id)));
    }

    protected void validateExistsByEmail(String email) {
        if (this.userRepository.existsByEmail(email)) {
            throw new ValidationException(MessageConstants.MSG_ERROR_VALIDATION,
                    String.format(MSG_ERROR_USER_EMAIL_EXISTS, email));
        }
    }
}
