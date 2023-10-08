package io.include9it.finance_aggregator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({IllegalStateException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiException> handleIllegalStateException(Exception e) {
        var badRequest = HttpStatus.BAD_REQUEST;

        var exception = new ApiException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, badRequest);
    }

    @ExceptionHandler({RuntimeException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiException> handleRuntimeException(Exception e) {
        var internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;

        var exception = new ApiException(
                internalServerError,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(exception, internalServerError);
    }
}
