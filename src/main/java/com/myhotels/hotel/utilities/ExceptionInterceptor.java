package com.myhotels.hotel.utilities;

import com.myhotels.hotel.dtos.errors.ApiError;
import com.myhotels.hotel.exceptions.DataNotFoundException;
import com.myhotels.hotel.exceptions.InvalidRequestException;
import lombok.extern.java.Log;
import org.springframework.dao.DataIntegrityViolationException;
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
    protected final ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warning(ex.getMessage());

        Throwable rootCause = ex.getMostSpecificCause();
        return new ResponseEntity<>(new ApiError("Invalid request. Please check the request and try again", rootCause.getMessage()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(DataNotFoundException.class)
    protected final ResponseEntity<ApiError> handleDataNotFoundException(DataNotFoundException ex) {
        log.warning(ex.getMessage());

        return new ResponseEntity<>(new ApiError(ex.getMessage(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    protected final ResponseEntity<ApiError> handleInvalidRequestException(InvalidRequestException ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected final ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.warning(ex.getMessage());

        Throwable cause = ex.getMostSpecificCause();
        return new ResponseEntity<>(new ApiError("Invalid request. Correct the root cause and try again.", cause.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
