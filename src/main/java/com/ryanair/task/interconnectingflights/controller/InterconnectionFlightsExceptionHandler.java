package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;

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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public List<FoundFlightsModel> badRequest() {
        log.error("Input values is null or empty");
        return Collections.emptyList();
    }
}
