package com.ryanair.task.interconnectingflights.services;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.analyzers.FlightScheduleAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service which is responsible to provide controller list of found flight models based on received filter.
 */
@Service
public class InterconnectingFlightsService implements FlightsService {
    private FlightScheduleAnalyzer flightScheduleAnalyzer;

    @Autowired
    public InterconnectingFlightsService(final FlightScheduleAnalyzer flightScheduleAnalyzer) {
        this.flightScheduleAnalyzer = flightScheduleAnalyzer;
    }

    @Override
    public List<FoundFlightsModel> getAppropriateFlights(final FlightFilterModel filter) {
        return flightScheduleAnalyzer.analyzeAndGetFinalListOfFlights(filter);
    }
}
