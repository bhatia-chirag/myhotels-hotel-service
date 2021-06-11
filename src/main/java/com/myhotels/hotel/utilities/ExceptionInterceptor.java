package com.myhotels.hotel.utilities;

import com.myhotels.hotel.dtos.errors.ApiError;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log
@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warning(ex.getMessage());

        Throwable rootCause = ex.getRootCause();
        if (rootCause == null) {
            return new ResponseEntity<>(new ApiError("Invalid request. Please check the request and try again", ex.getMessage()), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new ApiError("Invalid request. Please check the request and try again", rootCause.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(DataNotFoundException.class)
    public final ResponseEntity<Object> handleDataNotFoundException(DataNotFoundException ex) {
        log.warning(ex.getMessage());

        return new ResponseEntity<>(new ApiError(ex.getMessage(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}
