package com.jeanbarcellos.project111.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jeanbarcellos.core.validation.Validator;

@Configuration
public class ValidatorConfig {

    @Bean
    Validator validator() {
        return new Validator();
    }
}
