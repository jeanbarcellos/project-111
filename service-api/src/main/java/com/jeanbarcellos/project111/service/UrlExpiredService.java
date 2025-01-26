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

    private final UrlRepository urlRepository;

    @Transactional
    public Integer deleteAllExpiredUrls() {
        var expiredUrls = this.getAllExpiredUrls();

        log.info(String.format("Removing all expired urls: %s urls", expiredUrls.size()));

        this.urlRepository.deleteAll(expiredUrls);

        return expiredUrls.size();
    }

    private List<Url> getAllExpiredUrls() {
        return this.urlRepository.findAll()
                .stream()
                .filter(Url::isExpired)
                .toList();
    }
}
