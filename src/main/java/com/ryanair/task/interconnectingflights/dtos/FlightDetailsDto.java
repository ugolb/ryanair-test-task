package com.ryanair.task.interconnectingflights.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@Builder
public class FlightDetailsDto implements Serializable {
    private String number;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
}
