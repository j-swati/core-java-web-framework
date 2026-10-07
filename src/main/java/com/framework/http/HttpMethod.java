package com.framework.http;

public enum HttpMethod {
    GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, UNKNOWN;

    public static HttpMethod fromString(String method){
        if(method == null) return UNKNOWN;

        try{
            return HttpMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}


// fromString to convert "GET" -> HttpMethod.GET