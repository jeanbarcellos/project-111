package com.jeanbarcellos.project111.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeanbarcellos.core.exception.NotFoundException;
import com.jeanbarcellos.core.validation.Validator;
import com.jeanbarcellos.project111.dto.UrlRequest;
import com.jeanbarcellos.project111.dto.UrlResponse;
import com.jeanbarcellos.project111.entity.Url;
import com.jeanbarcellos.project111.mapper.UrlMapper;
import com.jeanbarcellos.project111.repository.UrlRepository;
import com.jeanbarcellos.project111.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    public static final String MSG_ERROR_URL_NOT_FOUND = "There is no user for the ID '%s' provided.";

    private final UserRepository userRepository;

    private final UrlRepository urlRepository;

    private final UrlMapper urlMapper;

    private final Validator validator;

    public List<UrlResponse> getAll() {
        return this.urlMapper.toResponseList(this.urlRepository.findAll());
    }

    public UrlResponse getById(String id) {
        return this.urlMapper.toResponse(this.findByIdOrThrow(id));
    }

    @Transactional
    public UrlResponse createShortUrl(UrlRequest request) {
        this.validator.validate(request);

        var user = this.userRepository.getReferenceById(request.getUserId());

        String hash = generateHash();

        var url = new Url(user, hash, request.getUrl());

        url = urlRepository.save(url);

        return this.urlMapper.toResponse(url);
    }

    private String generateHash() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private Url findByIdOrThrow(String id) {
        return this.urlRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(MSG_ERROR_URL_NOT_FOUND, id)));
    }

}