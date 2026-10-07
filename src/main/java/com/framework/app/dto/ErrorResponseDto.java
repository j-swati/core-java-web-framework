package com.framework.app.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto (
        int status,
        String error,
        String message,
        LocalDateTime timestamp
){
    public ErrorResponseDto(int status, String error, String message){
        this(status, error, message, LocalDateTime.now());
    }
}


// we provided a custom constructor here
// we now have  two ways to create ErrorResponseDto

//new ErrorResponseDto( 404, "Not Found", "User does not exist", LocalDateTime.now() );

//new ErrorResponseDto( 404, "Not Found", "User does not exist");
