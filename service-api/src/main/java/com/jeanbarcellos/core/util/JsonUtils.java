package com.jeanbarcellos.core.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jeanbarcellos.core.exception.ApplicationException;

import lombok.extern.slf4j.Slf4j;

/**
 * JSON Utils
 *
 * @author Jean Silva de Barcellos (jeanbarcellos@hotmail.com)
 */
@Slf4j
public class JsonUtils {

    private static final String ERROR_SERRIALIZE = "Erro serializar Objeto para JSON";
    private static final String ERROR_DESERRIALIZE = "Erro ao desserializar do JSON para Objeto";

    private JsonUtils() {
    }

    private static ObjectMapper getObjectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        return mapper;
    }

    public static String toJson(Object obj) {
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(ERROR_SERRIALIZE, e);
        }
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return getObjectMapper().readValue(json, valueType);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApplicationException(ERROR_DESERRIALIZE, e);
        }
    }
}
