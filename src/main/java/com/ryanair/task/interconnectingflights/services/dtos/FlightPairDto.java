package com.ryanair.task.interconnectingflights.services.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightPairDto {
    private AvailableRoutsDto firstLeg;
    private AvailableRoutsDto secondLeg;
}
