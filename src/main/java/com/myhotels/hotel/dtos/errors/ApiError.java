package com.myhotels.hotel.dtos.errors;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {

    private String message;
    private String rootCause;
    private LocalDateTime timeStamp;

    public ApiError (String message, String rootCause) {
        this.message = message;
        this.rootCause = rootCause;
        timeStamp = LocalDateTime.now();
    }
}
