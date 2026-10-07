package com.framework.exception;

import com.framework.http.HttpStatus;

public class EntityNotFoundException extends RuntimeException{
    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public EntityNotFoundException(String message){
        super(message);
    }

    public HttpStatus getStatus(){
        return status;
    }
}


// bcz extends RuntimeException, it's an unchecked exception => try catch / throws not mandatory
// this exception is carrying 2 pieces of information - message, status
