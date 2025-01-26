package com.jeanbarcellos.project111.service;

import org.springframework.stereotype.Service;

import com.jeanbarcellos.project111.mapper.UrlMapper;
import com.jeanbarcellos.project111.repository.UrlRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *  Redirecionamento
 *  - Usuário acessa https://short.url/abc123.*
 *    - Backend:
 *      - Consulta o cache (Redis) para encontrar a URL original.
 *      - Caso não encontre, busca no banco de dados.
 *      - Verifica a validade da URL (não expirada).
 *      - Retorna um redirecionamento HTTP 302 para a URL original.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlRedirectService {

    private final UrlRepository urlRepository;

    private final UrlMapper urlMapper;

    public String redirect(String shortId){
        return "";
    }
}
