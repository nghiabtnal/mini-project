/*
 * @(#)JsonUtils.java
 *
 * Copyright(c) 2020-2021 ZENSHO HOLDINGS Co., Ltd. All Rights Reserved.
 */
package org.example.miniproject.libs.utils;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JsonUtils {


    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /** objectMapper */
    private static ObjectMapper objectMapper;

    static {
        // set init object mapper
        objectMapper = JsonMapper.builder()
                .defaultPropertyInclusion(
                        JsonInclude.Value.construct(Include.NON_NULL, Include.USE_DEFAULTS))
                .build();
    }

    /**
     * reset the {@link ObjectMapper}.
     * 
     * @param objectMapper The {@link ObjectMapper}
     */
    public static void resetObjectMapper(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
    }

    /**
     * non-public constructor.
     */
    private JsonUtils() {
    }

    /**
     * serialize JavaBean to Json String.
     * 
     * @param target the JavaBean
     * @return a Json String
     * @throws IOException if Json generating problem occurs
     */
    public static String serialize(Object target) throws IOException {
        return objectMapper.writeValueAsString(target);
    }

    /**
     * serialize JavaBean to Json String.<br>
     * if Json generating problem occurs, null String will be return.
     * 
     * @param target the JavaBean
     * @return a Json String
     */
    public static String serializeQuietly(Object target) {
        try {
            return serialize(target);
        } catch (IOException e) {
            logger.error("Json generate error.", e);
            return null;
        }
    }

    /**
     * convert JavaBean to the specified type.
     * 
     * @param target the JavaBean
     * @param type   the specified type
     * @return the instance of the specified type
     */
    public static <T> T convertValue(Object target, Class<T> type) {
        return objectMapper.convertValue(target, type);
    }

    /**
     * deserialize Json String at InputStream to JavaBean.
     * 
     * @param inputStream inputStream that to read Json String
     * @param type        the type of JavaBean that to return
     * @return JavaBean
     * @throws IOException if a low-level I/O or Json parse problem occurs
     */
    public static <T> T deserialize(InputStream inputStream, Class<T> type) throws IOException {
        return objectMapper.readValue(inputStream, type);
    }

    /**
     * deserialize Json String to JavaBean.
     * 
     * @param inputStream inputStream that to read Json String
     * @param type        the type of JavaBean that to return
     * @return JavaBean
     * @throws IOException if a low-level I/O or Json parse problem occurs
     */
    public static <T> T deserialize(String target, Class<T> type) throws IOException {
        return objectMapper.readValue(target, type);
    }

    /**
     * deserialize Json String to JavaBean.<br>
     * if a low-level I/O or Json parse problem occurs,null Object will be return.
     * 
     * @param inputStream inputStream that to read Json String
     * @param type        the type of JavaBean that to return
     * @return JavaBean
     */
    public static <T> T deserializeQuietly(String target, Class<T> type) {
        try {
            return deserialize(target, type);
        } catch (IOException e) {
            logger.error("Json parse error.", e);
            return null;
        }
    }
}
