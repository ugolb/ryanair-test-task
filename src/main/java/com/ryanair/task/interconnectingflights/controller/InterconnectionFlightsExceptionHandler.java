package com.ryanair.task.interconnectingflights.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

/**
 * Exception handler which catches exceptions and map them to proper HTTP status code.
 */
@Slf4j
@ControllerAdvice
public class InterconnectionFlightsExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RestClientException.class)
    public void restClientIssue() {
        log.error("Failed to reach external service.");
    }
}
