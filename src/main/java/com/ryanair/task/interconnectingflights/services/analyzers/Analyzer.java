package com.ryanair.task.interconnectingflights.services.analyzers;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;

import java.util.List;

/**
 * Analyzer contains methods to analyse routes and flight schedules.
 */
public interface Analyzer {

    /**
     * Method should implement analyze and filtering logic for routes and flight schedules.
     *
     * @return FoundFlightsModel object.
     */
    List<FoundFlightsModel> analyze(final FlightFilterModel flightFilterModel);
}
