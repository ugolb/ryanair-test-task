package com.ryanair.task.interconnectingflights.services;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.analyzers.FlightScheduleAnalyzer;
import com.ryanair.task.interconnectingflights.services.dao.FlightsInfoDaoImpl;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.services.dtos.FlightPairDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterconnectingFlightsService implements FlightsService {
    List<FlightPairDto> flightPairDtoList = new ArrayList<>();
    private FlightScheduleAnalyzer flightScheduleAnalyzer;
    private FlightsInfoDaoImpl flightsInfoDao;

    @Autowired
    public InterconnectingFlightsService(final FlightScheduleAnalyzer flightScheduleAnalyzer,
                                         final FlightsInfoDaoImpl flightsInfoDao) {
        this.flightScheduleAnalyzer = flightScheduleAnalyzer;
        this.flightsInfoDao = flightsInfoDao;
    }

    @Override
    public List<FoundFlightsModel> getAppropriateFlights(final FlightFilterModel filter) {
        List<AvailableRoutsDto> allAvailableRoutes = flightsInfoDao.getAllAvailableRoutes();
        List<AvailableRoutsDto> collect1 = allAvailableRoutes.stream()
                .filter(route -> departuresIsEqual(filter, route) || arrivalsIsEqual(filter, route))
                .collect(Collectors.toList());

        List<AvailableRoutsDto> list = new ArrayList<>();

        for (AvailableRoutsDto dto : collect1) {
            //direct
            if ((departuresIsEqual(filter, dto) && arrivalsIsEqual(filter, dto)) && isNullOrEmpty(dto.getConnectingAirport())) {
                flightPairDtoList.add(FlightPairDto.builder().firstLeg(dto).build());
            }
            //pair fainder
            else if ((departuresIsEqual(filter, dto) && !arrivalsIsEqual(filter, dto) && isNullOrEmpty(dto.getConnectingAirport()))) {
                addToListIfPairExist(collect1, dto.getAirportTo(), filter.getArrival(), dto);
            }
            if ((departuresIsEqual(filter, dto) && arrivalsIsEqual(filter, dto)) && !isNullOrEmpty(dto.getConnectingAirport())) {
                AvailableRoutsDto firstLeg = AvailableRoutsDto.builder().airportFrom(dto.getAirportFrom()).airportTo(dto.getConnectingAirport()).build();
                AvailableRoutsDto secondLeg = AvailableRoutsDto.builder().airportFrom(dto.getConnectingAirport()).airportTo(dto.getAirportTo()).build();
                flightPairDtoList.add(FlightPairDto.builder().firstLeg(firstLeg).secondLeg(secondLeg).build());
            }
        }


        return flightScheduleAnalyzer.analyze(filter);
    }

    private void addToListIfPairExist(List<AvailableRoutsDto> list, String departure, String arrival, AvailableRoutsDto firstLeg) {
        for (AvailableRoutsDto secondLegDto : list) {
            if (departuresIsEqual(secondLegDto.getAirportFrom(), departure)
                    && arrivalsIsEqual(secondLegDto.getAirportTo(), arrival)
                    && isNullOrEmpty(secondLegDto.getConnectingAirport())) {
                flightPairDtoList.add(
                        FlightPairDto.builder()
                                .firstLeg(firstLeg)
                                .secondLeg(secondLegDto)
                                .build());
            }
        }
    }

    private boolean isNullOrEmpty(String valueToCheck) {
        return StringUtils.isEmpty(valueToCheck);
    }

    private boolean departuresIsEqual(FlightFilterModel filter, AvailableRoutsDto route) {
        return route.getAirportFrom().equals(filter.getDeparture());
    }

    private boolean departuresIsEqual(String dtoDeparture, String filterDeparture) {
        return dtoDeparture.equals(filterDeparture);
    }

    private boolean arrivalsIsEqual(FlightFilterModel filter, AvailableRoutsDto route) {
        return route.getAirportTo().equals(filter.getArrival());
    }

    private boolean arrivalsIsEqual(String dtoDeparture, String filterDeparture) {
        return dtoDeparture.equals(filterDeparture);
    }
}
