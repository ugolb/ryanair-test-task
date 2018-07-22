package com.ryanair.task.interconnectingflights.services.dao;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableSchedulesDto;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface FlightsInfoDao {

    /**
     * Return all available flights routes.
     *
     * @return list of AvailableRoutsDto.
     */
    List<AvailableRoutsDto> getAllAvailableRoutes();

    /**
     * Returns flights schedule based on filtering data provided by user.
     *
     * @return AvailableSchedulesDto object.
     */
    AvailableSchedulesDto getFlightsSchedule(@NotNull FlightFilterModel filter, String departure, String arrival);
}
