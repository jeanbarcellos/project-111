package com.jeanbarcellos.project111.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponse {

    private String hash;
    private String targetUrl;
    private LocalDateTime createdAt;
    private LocalDate expiresAt;

}
