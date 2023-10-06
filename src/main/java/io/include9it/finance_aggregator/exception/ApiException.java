package io.include9it.finance_aggregator.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiException(
        String message,
        HttpStatus httpStatus,
        ZonedDateTime timestamp
) {
    public ApiException(HttpStatus httpStatus, ZonedDateTime timestamp) {
        this(null, httpStatus, timestamp);
    }
}
