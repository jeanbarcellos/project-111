package com.jeanbarcellos.project111.service;

import org.springframework.stereotype.Service;

import com.jeanbarcellos.project111.repository.UrlRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Redirecionamento
 * - Usuário acessa https://short.url/abc123.*
 * - Backend:
 * - Consulta o cache (Redis) para encontrar a URL original.
 * - Caso não encontre, busca no banco de dados.
 * - Verifica a validade da URL (não expirada).
 * - Retorna um redirecionamento HTTP 302 para a URL original.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlRedirectService {

    private static final String MSG_URL_ACCESSED = "URL '%s' acessada";

    private final UrlRepository urlRepository;

    public String getTargetUrl(String shortId) {
        var url = this.urlRepository.findById(shortId)
                .orElse(null);

        if (url == null) {
            return null;
        }

        if (url.isExpired()) {
            return null;
        }

        logAccess(shortId);

        return url.getTargetUrl();
    }

    private void logAccess(String shortId) {
        // Enviar para algum serviço de registro
        log.info(String.format(MSG_URL_ACCESSED, shortId));
    }
}
