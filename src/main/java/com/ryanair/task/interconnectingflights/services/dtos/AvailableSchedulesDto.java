package com.ryanair.task.interconnectingflights.services.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class AvailableSchedulesDto implements Serializable {
    private Integer month;
    private List<ScheduleDayDto> days;
}
