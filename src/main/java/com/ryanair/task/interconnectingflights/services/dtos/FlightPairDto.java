package com.ryanair.task.interconnectingflights.services.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FlightPairDto {
    private AvailableRoutsDto firstLeg;
    private AvailableRoutsDto secondLeg;
    private LocalDateTime departureDateTime;
    private LocalDateTime arrivalDateTime;
}
