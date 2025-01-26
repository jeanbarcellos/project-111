package com.jeanbarcellos.project111.service;

import java.util.List;
import java.util.Random;
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
public class UrlShortenerService {

    private static final String MSG_ERROR_URL_NOT_FOUND = "There is no user for the ID '%s' provided.";
    private static final String MSG_SHORTENED_URL_CREATED = "Shortened URL created: %s";

    private static final Integer HASH_LENGHT = 6;
    private static final Integer EXPIRATION_IN_DAYS = 30;

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

        String shortUrl = generateHashWithCollisionValidation();

        var url = new Url(user, shortUrl, request.getUrl(), EXPIRATION_IN_DAYS);

        this.urlRepository.save(url);

        log.info(String.format(MSG_SHORTENED_URL_CREATED, shortUrl));

        return this.urlMapper.toResponse(url);
    }


    @Transactional
    public void deleteAll() {
        this.urlRepository.deleteAll();
    }

    private Url findByIdOrThrow(String id) {
        return this.urlRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(MSG_ERROR_URL_NOT_FOUND, id)));
    }

    /**
     * Gerara hash com validação de colisão.
     *
     * @return
     */
    private String generateHashWithCollisionValidation() {
        String shortUrl;
        do {
            shortUrl = this.generateHash();
        } while (this.urlRepository.existsById(shortUrl)); // Verifica colisão
        return shortUrl;
    }

    /**
     * Gerar Hash - Operação 1
     *
     * Usando UUID e truncando para os primeiros 6 caracteres.
     *
     * Embora não seja 100% seguro, é improvável que haja colisão para volumes
     * moderados.
     *
     * @return String Hash
     */
    protected String generateHash() {
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, HASH_LENGHT);
    }

    protected String generateHash2() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder();

        for (int i = 0; i < HASH_LENGHT; i++) {
            shortUrl.append(chars.charAt(random.nextInt(chars.length())));
        }
        return shortUrl.toString();
    }

}