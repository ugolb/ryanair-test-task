package com.ryanair.task.interconnectingflights.models;

import com.ryanair.task.interconnectingflights.utils.DateTimeUtil;
import com.ryanair.task.interconnectingflights.utils.Validator;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * FlightFilterModel represents data received from user to find appropriate flight schedule
 */
@Getter
public class FlightFilterModel {
    Validator validator = new Validator();

    private String departure;
    private String arrival;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;

    public FlightFilterModel(String departure, String arrival, String departureDateTime, String arrivalDateTime) {

        this.departure = validator.checkIfNullOrEmpty(departure);
        this.arrival = validator.checkIfNullOrEmpty(arrival);
        this.departureDateTime = DateTimeUtil.convertStringToLocalDateTime(validator.checkIfNullOrEmpty(departureDateTime));
        this.arrivalDateTime = DateTimeUtil.convertStringToLocalDateTime(validator.checkIfNullOrEmpty(arrivalDateTime));
    }
}
