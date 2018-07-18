package com.ryanair.task.interconnectingflights.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class ScheduleDaysDto implements Serializable {
    private int day;
    private List<FlightDetailsDto> flights;
}
