package com.jeanbarcellos.project111.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeanbarcellos.project111.entity.Url;
import com.jeanbarcellos.project111.repository.UrlRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlExpiredService {

    private static final String MSG_REMOED_ALL_EXPIRED = "All expired URLs have been removed: %s urls";

    private final UrlRepository urlRepository;

    @Transactional
    public Integer deleteAllExpiredUrls() {
        var expiredUrls = this.getAllExpiredUrls();

        this.urlRepository.deleteAll(expiredUrls);

        log.info(String.format(MSG_REMOED_ALL_EXPIRED, expiredUrls.size()));

        return expiredUrls.size();
    }

    private List<Url> getAllExpiredUrls() {
        return this.urlRepository.findAll()
                .stream()
                .filter(Url::isExpired)
                .toList();
    }
}
