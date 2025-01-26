package com.jeanbarcellos.project111.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeanbarcellos.project111.service.UrlRedirectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/redirect")
@Tag(name = "URL Redirect", description = "Redirect URLs")
public class UrlRedirectController {

    private final UrlRedirectService redirectService;

    @GetMapping("/{shortId}")
    @Operation(summary = "Redirect")
    public ResponseEntity<String> redirect(@PathVariable String shortId) {
        var url = this.redirectService.redirect(shortId);
        return ResponseEntity.ok(url);
    }
}
