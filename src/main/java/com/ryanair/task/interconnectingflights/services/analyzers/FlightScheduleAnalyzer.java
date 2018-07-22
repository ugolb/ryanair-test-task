package com.ryanair.task.interconnectingflights.services.analyzers;

import com.ryanair.task.interconnectingflights.constants.CommonConstants;
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

/**
 * This component analyze data from two external services based on received filter
 * and combine result for each flight to FoundFlightsModel object.
 */
@Component
public class FlightScheduleAnalyzer extends CommonAnalyzer implements Analyzer {

    private List<AvailableRoutsDto> allAvailableRoutes;
    private FlightsInfoDaoImpl flightsInfoDao;

    @Autowired
    public FlightScheduleAnalyzer(FlightsInfoDaoImpl flightsInfoDao) {
        this.flightsInfoDao = flightsInfoDao;
        allAvailableRoutes = flightsInfoDao.getAllAvailableRoutes();
    }

    /**
     * Analyze data received from different services according to main rules:
     * Direct flight rules:
     * 1) Departure date and time should not be less then departure time in request filter
     * 2) Arrival date and time should not be more then arrival date time in request.
     * <p>
     * Interconnecting flight rules:
     * 1) Departure date and time of first flight should not be less then departure time in request filter
     * 2) Arrival date and time of second flight should not be more then arrival date time in request.
     * 3) Difference between first flight arrival and second flight departure must be equal or more then two hours.
     * 4) Allowed only one stop.
     *
     * @param filter - received filter.
     * @return - list of FoundFlightsModel objects.
     */
    @Override
    public List<FoundFlightsModel> analyzeAndGetFinalListOfFlights(final FlightFilterModel filter) {
        List<FlightPairDto> flightPairDtoList = getFlightPairs(filter, allAvailableRoutes);

        List<FoundFlightsModel> foundFlights = new ArrayList<>();

        flightPairDtoList.parallelStream().forEach(currentRoute -> {
            if (isInterconnectingFlight(currentRoute)) {
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

    /**
     * Scheduled method which repopulates allAvailableRoutes list once per hour.
     * It helps to avoid call each time FlightRoutes service what is improving service performance.
     */
    @Scheduled(cron = CommonConstants.TRIGGER_TIME)
    private void repopulateRoutes() {
        allAvailableRoutes = flightsInfoDao.getAllAvailableRoutes();
    }
}
