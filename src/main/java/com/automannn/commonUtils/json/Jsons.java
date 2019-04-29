package com.automannn.commonUtils.json;

import com.automannn.commonUtils.util.Strings;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author automannn@163.com
 * @time 2019/4/29 10:50
 */
public class Jsons {
    public static final Jsons EXCLUDE_EMPTY = new Jsons(JsonInclude.Include.NON_EMPTY);

    public static final Jsons EXCLUDE_DEFAULT = new Jsons(JsonInclude.Include.NON_DEFAULT);

    public static final Jsons DEFAULT = new Jsons();

    private ObjectMapper mapper;

    private Jsons() {
        mapper = new ObjectMapper();
        // ignore attributes exists in json string, but not in java object when deserialization
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private Jsons(JsonInclude.Include include) {
        mapper = new ObjectMapper();
        // set serialization feature
        mapper.setSerializationInclusion(include);
        // ignore attributes exists in json string, but not in java object when deserialization
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public String toJson(Object target) {
        try {
            return mapper.writeValueAsString(target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String json, Class<T> target) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) mapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
