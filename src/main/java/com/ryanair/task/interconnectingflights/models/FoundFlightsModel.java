package com.ryanair.task.interconnectingflights.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FoundFlightsModel {
    private int stops;
    private List<FlightLegModel> legs;
}
