package com.inheritech.it.factory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-04-26 17:50:18
 */
public class ParseStringFactory {
    private ParseStringFactory() {
    }

    public static Map<String, Object> parse(String body, Class<Map> mapClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(body, mapClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("body parse exception");
        }
    }
}