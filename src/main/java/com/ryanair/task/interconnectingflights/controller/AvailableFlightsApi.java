package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping(value = "/find-available-flights")
public interface AvailableFlightsApi {

    @RequestMapping(value = "/interconnections", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody ResponseEntity<List<FoundFlightsModel>> loginUser(
            @RequestParam("departureAirport") String departureAirport,
            @RequestParam("arrivalAirport") String arrivalAirport,
            @RequestParam("departureDateTime") LocalDateTime departureDateTime,
            @RequestParam("arrivalDateTime") LocalDateTime arrivalDateTime);

}
