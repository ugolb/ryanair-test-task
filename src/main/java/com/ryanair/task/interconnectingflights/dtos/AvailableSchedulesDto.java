package com.ryanair.task.interconnectingflights.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class AvailableSchedulesDto implements Serializable {
    private Integer month;
    private List<ScheduleDaysDto> days;
}
