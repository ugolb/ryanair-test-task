package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.models.FlightLegModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
public class AvailableFlightsController implements AvailableFlightsApi {
    @Override
    public ResponseEntity<List<FoundFlightsModel>> loginUser(
            @RequestParam("departureAirport") String departureAirport,
            @RequestParam("arrivalAirport") String arrivalAirport,
            @RequestParam("departureDateTime") LocalDateTime departureDateTime,
            @RequestParam("arrivalDateTime") LocalDateTime arrivalDateTime) {
        return ResponseEntity.ok(getMock());

    }

    public List<FoundFlightsModel> getMock() {
        FoundFlightsModel build = FoundFlightsModel.builder()
                .stops(0)
                .legs(Collections.singletonList(FlightLegModel.builder()
                        .arrivalAirport("WAS")
                        .departureAirport("WRO")
                        .arrivalDateTime(LocalDateTime.now())
                        .departureDateTime(LocalDateTime.now())
                        .build()))
                .build();
        return Collections.singletonList(build);
    }
}
