package com.framework.http;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private final OutputStream output;

    public HttpResponse(OutputStream output) {
        this.output = output;
    }

    // Sends an object serialized to JSON via Jackson
    public void sendJson(HttpStatus status, Object data) {
        try {
            String jsonString = JsonSerializer.getInstance().writeValueAsString(data);
            byte[] bodyBytes = jsonString.getBytes(StandardCharsets.UTF_8);

            PrintWriter writer = new PrintWriter(output, false, StandardCharsets.UTF_8);
            writer.printf("HTTP/1.1 %d %s\r\n", status.getCode(), status.getMessage());
            writer.println("Content-Type: application/json");
            writer.println("Content-Length: " + bodyBytes.length);
            writer.println("Connection: close");
            writer.println();
            writer.flush();

            output.write(bodyBytes);
            output.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error writing HTTP JSON response", e);
        }
    }

    // Sends raw text or manually constructed JSON strings
    public void sendText(HttpStatus status, String text) {
        try {
            byte[] bodyBytes = text.getBytes(StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(output, false, StandardCharsets.UTF_8);
            writer.printf("HTTP/1.1 %d %s\r\n", status.getCode(), status.getMessage());
            writer.println("Content-Type: text/plain");
            writer.println("Content-Length: " + bodyBytes.length);
            writer.println("Connection: close");
            writer.println();
            writer.flush();

            output.write(bodyBytes);
            output.flush();
            // sends the body
        } catch (Exception e) {
            throw new RuntimeException("Error writing HTTP text response", e);
        }
    }
}

// HttpResponse -- takes Java data -> turns it into raw HTTP data sent back to the client.
// OutputStream bcz ur server needs to send data out to the client

// JsonSerializer.getInstance().writeValueAsString(data);
// User("John", 25); --> {"name":"John","age":25}

// jsonString.getBytes(StandardCharsets.UTF_8);
// string -> bytes bcz network ultimately sends bytes

// PrintWriter makes it convenient to write text to the OutputStream.
// Without it, you'd have to manually convert every header to bytes.


// writer.flush() basically means:  Send the data I've written so far to the underlying output stream.