package net.easyappsec.block.json.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.easyappsec.block.json.JsonMapper;
import net.easyappsec.block.json.MappingException;

public class JacksonJsonMapper<T> implements JsonMapper<T> {

    private ObjectMapper objectMapper = new ObjectMapper();

    private void init() {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public JacksonJsonMapper() {
        init();
    }

    public String getJson(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException ex) {
            throw new MappingException(ex);
        }
    }

    public T getObject(String string) {
        throw new UnsupportedOperationException("Get object is not supported!");
    }
}
