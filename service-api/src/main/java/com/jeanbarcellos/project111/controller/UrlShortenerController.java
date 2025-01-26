package com.jeanbarcellos.project111.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeanbarcellos.project111.dto.UrlRequest;
import com.jeanbarcellos.project111.dto.UrlResponse;
import com.jeanbarcellos.project111.service.UrlShortenerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/urls/shortener")
@Tag(name = "URL Shortener", description = "Shortener URLs")
public class UrlShortenerController {

    private final UrlShortenerService urlService;

    @GetMapping
    @Operation(summary = "Get all URLs")
    public ResponseEntity<List<UrlResponse>> getAll() {
        return ResponseEntity.ok(this.urlService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get URL by ID")
    public ResponseEntity<UrlResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(this.urlService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create Short URL")
    public ResponseEntity<UrlResponse> createShortUrl(@RequestBody UrlRequest request) {
        return ResponseEntity.ok(this.urlService.createShortUrl(request));
    }

    @DeleteMapping
    @Operation(summary = "Delete all URLs")
    public ResponseEntity<?> deleteAll() {
        this.urlService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}