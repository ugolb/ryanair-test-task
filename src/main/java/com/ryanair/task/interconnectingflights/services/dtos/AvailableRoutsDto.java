package com.ryanair.task.interconnectingflights.services.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * AvailableRoutsDto representation of one one particular flight route.
 */
@Data
@Builder
public class AvailableRoutsDto {

    @NotNull
    private String airportFrom;
    @NotNull
    private String airportTo;
    private String connectingAirport;
    private String operator;
    private String group;
    private boolean newRoute;
    private boolean seasonalRoute;
}
