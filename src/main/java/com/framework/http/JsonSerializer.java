package com.framework.http;

import tools.jackson.databind.json.JsonMapper;

public class JsonSerializer {

    private static final JsonMapper jackson = JsonMapper.builder().build();

    private JsonSerializer() {
    }

    public static JsonMapper getInstance() {
        return jackson;
    }
}