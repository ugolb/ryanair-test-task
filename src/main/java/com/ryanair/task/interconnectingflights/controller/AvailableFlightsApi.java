package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping(value = "/find-available-flights")
public interface AvailableFlightsApi {

    @RequestMapping(value = "/interconnections", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    ResponseEntity<List<FoundFlightsModel>> loginUser(@ModelAttribute FlightFilterModel flightFilterModel);
}
//http://localhost:8080/find-available-flights/interconnections?departure=DUB&arrival=WRO&departureDateTime=2016-03-01T07:00&arrivalDateTime=2016-03-03T21:00