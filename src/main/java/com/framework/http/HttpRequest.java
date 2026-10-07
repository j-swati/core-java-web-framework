package com.framework.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> headers = new HashMap<>();
    private final String body;

    public HttpRequest(InputStream input) throws Exception {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8));
        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isEmpty()) {
            this.method = HttpMethod.UNKNOWN;
            this.path = "";
            this.body = "";
            return;
        }

        // Parse Request Line: "POST /api/users HTTP/1.1"
        String[] parts = requestLine.split(" ");
        this.method = HttpMethod.fromString(parts[0]);
        this.path = parts.length > 1 ? parts[1] : "/";

        // Parse Headers
        String line;
        int contentLength = 0;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            int idx = line.indexOf(":");
            if (idx != -1) {
                String headerName = line.substring(0, idx).trim();
                String headerValue = line.substring(idx + 1).trim();
                headers.put(headerName.toLowerCase(), headerValue);

                if ("content-length".equalsIgnoreCase(headerName)) {
                    contentLength = Integer.parseInt(headerValue);
                }
            }
        }

        // Parse Body if Content-Length exists
        if (contentLength > 0) {
            char[] bodyChars = new char[contentLength];
            int read = 0;
            while (read < contentLength) {
                int count = reader.read(bodyChars, read, contentLength - read);
                if (count == -1) break;
                read += count;
            }
            this.body = new String(bodyChars);
        } else {
            this.body = "";
        }
    }

    public HttpMethod getMethod() { return method; }
    public String getPath() { return path; }
    public String getBody() { return body; }
    public Map<String, String> getHeaders() { return headers; }

    // Jackson integration: deserialize JSON payload into Java target class/record
    public <T> T getBodyAs(Class<T> targetClass) {
        if (body.isBlank()) {
            throw new IllegalArgumentException("Request body is empty");
        }
        return JsonSerializer.getInstance().readValue(body, targetClass);
    }
}


// request parser
// take raw data coming from a client through an i/p stream and turn it into nice Java Object
// HttpRequest reads the InputStream and parses it
// method, path, headers, body

// The InputStream contains bytes.
// new InputStreamReader(input, StandardCharsets.UTF_8)  -- convert bytes -> characters

// The first line of an HTTP request is called the request line.

// Content-Length -- size of the body in bytes, not number of lines or characters.

// It's not guaranteed that reader.read(...) will always read everything.