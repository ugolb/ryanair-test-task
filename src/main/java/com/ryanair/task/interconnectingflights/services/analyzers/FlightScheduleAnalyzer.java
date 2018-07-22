package com.ryanair.task.interconnectingflights.services.analyzers;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.dao.FlightsInfoDaoImpl;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableSchedulesDto;
import com.ryanair.task.interconnectingflights.services.dtos.FlightPairDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlightScheduleAnalyzer extends CommonAnalyzer implements Analyzer {

    private List<AvailableRoutsDto> allAvailableRoutes;
    private FlightsInfoDaoImpl flightsInfoDao;

    @Autowired
    public FlightScheduleAnalyzer(FlightsInfoDaoImpl flightsInfoDao) {
        this.flightsInfoDao = flightsInfoDao;
        allAvailableRoutes = flightsInfoDao.getAllAvailableRoutes();
    }

    @Override
    public List<FoundFlightsModel> analyzeAndGetFinalListOfFlights(final FlightFilterModel filter) {
        List<FlightPairDto> flightPairDtoList = getFilteredRoutes(filter, allAvailableRoutes);

        List<FoundFlightsModel> foundFlights = new ArrayList<>();

        flightPairDtoList.parallelStream().forEach(currentRoute -> {
            if (dtoHasTwoLegs(currentRoute)) {
                AvailableSchedulesDto flightsScheduleFirstLeg = flightsInfoDao
                        .getFlightsSchedule(filter, currentRoute.getFirstLeg().getAirportFrom(),
                                currentRoute.getFirstLeg().getAirportTo());
                AvailableSchedulesDto flightsScheduleSecondLeg = flightsInfoDao
                        .getFlightsSchedule(filter, currentRoute.getSecondLeg().getAirportFrom(),
                                currentRoute.getSecondLeg().getAirportTo());

                List<FoundFlightsModel> analyzedInterconnectedFlight = getAnalyzedInterconnectedFlight(
                        flightsScheduleFirstLeg, flightsScheduleSecondLeg, currentRoute, filter);
                foundFlights.addAll(analyzedInterconnectedFlight);
            } else {
                final AvailableSchedulesDto flightsSchedule = flightsInfoDao.getFlightsSchedule(filter,
                        currentRoute.getFirstLeg().getAirportFrom(), currentRoute.getFirstLeg().getAirportTo());
                foundFlights.addAll(getAnalyzedDirectFlight(flightsSchedule, currentRoute, filter));
            }
        });
        return foundFlights;
    }

    @Scheduled(cron = "0 0/60 * * * ?")
    private void repopulateRoutes() {
        allAvailableRoutes = flightsInfoDao.getAllAvailableRoutes();
    }
}
