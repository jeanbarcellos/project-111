package com.jeanbarcellos.project111.controller;

import java.net.URI;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/v1/urls/redirect")
@Tag(name = "URL Redirect", description = "Redirect URLs")
public class UrlRedirectController {

    private final UrlRedirectService redirectService;

    @GetMapping("/{shortId}")
    @Operation(summary = "Redirect")
    public ResponseEntity<Void> redirect(@PathVariable String shortId) {
        var url = this.redirectService.getTargetUrl(shortId);

        if (ObjectUtils.isEmpty(url)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                    // .status(HttpStatus.MOVED_PERMANENTLY.value()) // 301 -> Permanente
                    .status(HttpStatus.FOUND.value()) // 302 -> Temporário
                    .location(URI.create(url))
                    .build();
    }
}
