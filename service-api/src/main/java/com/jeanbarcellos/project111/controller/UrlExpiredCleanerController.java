package com.jeanbarcellos.project111.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeanbarcellos.project111.service.UrlExpiredService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/urls/cleaner")
@Tag(name = "URL Expired Cleaner", description = "Deleting expired URLs")
public class  UrlExpiredCleanerController {

    private final UrlExpiredService urlExpiredService;

    @DeleteMapping
    @Operation(summary = "Delete all expired URLs")
    public void cleanExpiredUrls() {
        this.urlExpiredService.deleteAllExpiredUrls();
    }
}
