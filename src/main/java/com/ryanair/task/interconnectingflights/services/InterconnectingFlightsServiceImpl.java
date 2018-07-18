package com.ryanair.task.interconnectingflights.services;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FlightLegModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.dao.FlightsInfoDaoImpl;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class InterconnectingFlightsServiceImpl implements InterconnectingFlightsService {

    private FlightsInfoDaoImpl flightsInfoDao;
//    List<AvailableRoutsDto> allAvailableRoutes;

    @Autowired
    public InterconnectingFlightsServiceImpl(FlightsInfoDaoImpl flightsInfoDao) {
        this.flightsInfoDao = flightsInfoDao;
//        allAvailableRoutes = flightsInfoDao.getAllAvailableRoutes();
    }

    @Override
    public List<FoundFlightsModel> getAppropriateFlights(final FlightFilterModel flightFilterModel) {
        //List<AvailableRoutsDto> allAvailableRoutes = flightsInfoDao.getAllAvailableRoutes();
        return getMock();
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
