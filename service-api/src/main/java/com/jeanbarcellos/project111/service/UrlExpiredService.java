package com.jeanbarcellos.project111.service;

import java.time.LocalDate;
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
    public void deleteAllExpiredUrls() {
        var expiredUrls = this.getAllExpiredUrls();

        this.urlRepository.deleteAll(expiredUrls);
    }

    private List<Url> getAllExpiredUrls() {
        return this.urlRepository.findAll()
              .stream()
              .filter(mapping -> mapping.getExpiresAt().isBefore(LocalDate.now()))
                .toList();
    }
}
