package com.ryanair.task.interconnectingflights.services;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;

import java.util.List;

public interface FlightsService {

    /**
     * Method should get appropriate flights according to user filter.
     *
     * @return list of FoundFlightsModel.
     */
    List<FoundFlightsModel> getAppropriateFlights(final FlightFilterModel flightFilterModel);
}
