package com.seo.mobilestore.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seo.mobilestore.exception.JsonProcessException;
import com.seo.mobilestore.service.JsonReaderService;
import org.springframework.stereotype.Service;

@Service
public class JsonReaderServiceImpl implements JsonReaderService {

    /**
     * Method read file json input
     *
     * @param content
     * @param valueType
     * @param <U>
     * @return content, valueType
     */
    @Override
    public <U> U readValue(String content, Class<U> valueType) {
        try {
            return new ObjectMapper().readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new JsonProcessException(e);
        }
    }
}
