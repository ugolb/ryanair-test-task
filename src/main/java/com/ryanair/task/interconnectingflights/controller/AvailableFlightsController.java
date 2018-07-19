package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.InterconnectingFlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AvailableFlightsController implements AvailableFlightsApi {

    private final InterconnectingFlightsService flightsService;

    @Autowired
    public AvailableFlightsController(InterconnectingFlightsService flightsService) {
        this.flightsService = flightsService;
    }

    @Override
    public ResponseEntity<List<FoundFlightsModel>> loginUser(@ModelAttribute final FlightFilterModel flightFilterModel) {
        return ResponseEntity.ok(flightsService.getAppropriateFlights(flightFilterModel));
    }
}
