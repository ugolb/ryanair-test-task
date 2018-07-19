package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public interface AvailableFlightsApi {

    @RequestMapping(value = "/interconnections", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    ResponseEntity<List<FoundFlightsModel>> loginUser(@ModelAttribute FlightFilterModel flightFilterModel);
}
