package com.jeanbarcellos.project111.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jeanbarcellos.project111.service.UrlExpiredService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UrlExpiredCleanerScheduler {

    private final UrlExpiredService urlExpiredService;

    @Scheduled(cron = "0 0 0 * * ?") // Executa diariamente
    public void cleanExpiredUrls() {
        // TODO estratégias de retry

        this.urlExpiredService.deleteAllExpiredUrls();
    }
}
