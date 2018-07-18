package com.ryanair.task.interconnectingflights.models;

import com.ryanair.task.interconnectingflights.utils.DateTimeUtil;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * FlightFilterModel represents data received from user to find appropriate flight schedule
 */
@Getter
public class FlightFilterModel {

    @NotNull
    private String departure;
    @NotNull
    private String arrival;
    @NotNull
    private LocalDateTime departureDateTime;
    @NotNull
    private LocalDateTime arrivalDateTime;

    public FlightFilterModel(@NotNull String departure,
                             @NotNull String arrival,
                             @NotNull String departureDateTime,
                             @NotNull String arrivalDateTime) {
        this.departure = departure;
        this.arrival = arrival;
        this.departureDateTime = DateTimeUtil.convertStringToLocalDateTime(departureDateTime);
        this.arrivalDateTime = DateTimeUtil.convertStringToLocalDateTime(arrivalDateTime);
    }
}
