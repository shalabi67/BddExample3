package com.bdd.tests.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonMapper<T> {
    public String toString(T object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public T toObject(String content, Class<T> classType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            T object = objectMapper.readValue(content, classType);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<T> toObjectList(String content, TypeReference<List<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<T> object = objectMapper.readValue(content, typeReference);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
