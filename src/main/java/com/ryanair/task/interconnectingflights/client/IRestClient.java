package com.ryanair.task.interconnectingflights.client;

import com.ryanair.task.interconnectingflights.dto.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.dto.AvailableSchedulesDto;

import java.util.List;

/**
 * This interface provides methods to send rest requests for another micro services
 */
public interface IRestClient {

    /**
     * This method return all available routs.
     *
     * @return list of AvailableRoutsDto objects.
     * @see AvailableRoutsDto
     */
    List<AvailableRoutsDto> getAllAvailableRouts();

    /**
     * This method returns list of available flight schedule based on user filter.
     *
     * @return list of AvailableSchedulesDto objects
     * @see AvailableSchedulesDto
     */
    List<AvailableSchedulesDto> getAllSchedulesByFilter();
}
