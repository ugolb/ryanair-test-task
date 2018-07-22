package com.ryanair.task.interconnectingflights.services;

import com.ryanair.task.interconnectingflights.constants.CommonConstants;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FlightLegModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.analyzers.FlightScheduleAnalyzer;
import com.ryanair.task.interconnectingflights.services.dao.FlightsInfoDaoImpl;
import com.ryanair.task.interconnectingflights.services.dtos.*;
import com.ryanair.task.interconnectingflights.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterconnectingFlightsService implements FlightsService {
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
        List<FlightPairDto> flightPairDtoList = getFilteredRoutes(filter, getInitiallyFilteredRoutes(filter));

        List<FoundFlightsModel> foundFlights = new ArrayList<>();

        for (FlightPairDto currentRoute : flightPairDtoList) {
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
        }
        return foundFlights;
    }

    private List<FoundFlightsModel> getAnalyzedInterconnectedFlight(final AvailableSchedulesDto availableScheduleFirstLeg,
                                                                    final AvailableSchedulesDto availableScheduleSecondLeg,
                                                                    final FlightPairDto flightPairDto,
                                                                    final FlightFilterModel filter) {
        if (Objects.isNull(availableScheduleFirstLeg) || Objects.isNull(availableScheduleSecondLeg)) {
            return Collections.emptyList();
        }

        final List<FlightDetailsDto> flightScheduleFirstLeg = getScheduleByDepartureDay(availableScheduleFirstLeg,
                flightPairDto);
        final List<FlightDetailsDto> flightScheduleSecondLeg = getScheduleByArrivalDay(availableScheduleSecondLeg,
                flightPairDto);

        List<FoundFlightsModel> listOfFoundFlightsModel = new ArrayList<>();

        for (FlightDetailsDto currentFlight : flightScheduleFirstLeg) {
            for (FlightDetailsDto secondLegSchedule : flightScheduleSecondLeg) {
                if (ChronoUnit.HOURS.between(currentFlight.getArrivalTime(),
                        secondLegSchedule.getDepartureTime()) >= CommonConstants.DIFF_BETWEEEN_FLIGHTS) {
                    FlightLegModel firstLeg = FlightLegModel.builder()
                            .departureAirport(flightPairDto.getFirstLeg().getAirportFrom())
                            .arrivalAirport(flightPairDto.getFirstLeg().getAirportTo())
                            .departureDateTime(LocalDateTime.of(filter.getDepartureDateTime().toLocalDate(),
                                    currentFlight.getDepartureTime()))
                            .arrivalDateTime(LocalDateTime.of(filter.getArrivalDateTime().toLocalDate(),
                                    currentFlight.getArrivalTime()))
                            .build();
                    FlightLegModel secondLeg = FlightLegModel.builder()
                            .departureAirport(flightPairDto.getSecondLeg().getAirportFrom())
                            .arrivalAirport(flightPairDto.getSecondLeg().getAirportTo())
                            .departureDateTime(LocalDateTime.of(filter.getDepartureDateTime().toLocalDate(),
                                    secondLegSchedule.getDepartureTime()))
                            .arrivalDateTime(LocalDateTime.of(filter.getArrivalDateTime().toLocalDate(),
                                    secondLegSchedule.getArrivalTime()))
                            .build();

                    FoundFlightsModel foundFlightsModel = FoundFlightsModel.builder()
                            .stops(CommonConstants.ONE_STOP)
                            .legs(Arrays.asList(firstLeg, secondLeg))
                            .build();

                    listOfFoundFlightsModel.add(foundFlightsModel);
                }
            }
        }

        return listOfFoundFlightsModel;

    }

    private List<FoundFlightsModel> getAnalyzedDirectFlight(final AvailableSchedulesDto schedulesDto,
                                                            final FlightPairDto flightPairDto, final FlightFilterModel filter) {

        final List<FlightDetailsDto> schedulesForParticularDate = getScheduleByDepartureDay(schedulesDto, flightPairDto);

        for (FlightDetailsDto currentFlight : schedulesForParticularDate) {
            if (DateTimeUtil.isInRange(currentFlight, flightPairDto)) {
                final FlightLegModel flightLegModel = buildFlightLeg(filter, currentFlight);
                return Collections.singletonList(
                        FoundFlightsModel.builder()
                                .stops(CommonConstants.WITHOUT_STOPS)
                                .legs(Collections.singletonList(flightLegModel))
                                .build()
                );
            }
        }
        return Collections.emptyList();
    }

    private List<FlightDetailsDto> getScheduleByDepartureDay(AvailableSchedulesDto schedulesDto, FlightPairDto flightPairDto) {
        for (ScheduleDayDto currentSchedule : schedulesDto.getDays()) {//TODO potentialy to refactor
            if (currentSchedule.getDay() == flightPairDto.getDepartureDateTime().getDayOfMonth()) {
                return currentSchedule.getFlights();
            }
        }
        return Collections.emptyList();
    }

    private List<FlightDetailsDto> getScheduleByArrivalDay(AvailableSchedulesDto schedulesDto, FlightPairDto flightPairDto) {
        List<ScheduleDayDto> days = schedulesDto.getDays();
        for (ScheduleDayDto currentSchedule : days) {
            if (currentSchedule.getDay() == flightPairDto.getArrivalDateTime().getDayOfMonth()) {
                return currentSchedule.getFlights();
            }
        }
        return Collections.emptyList();
    }

    private void addIfFlightExist(List<FoundFlightsModel> foundFlights, FoundFlightsModel analyzedDirectFlight) {
        if (Objects.nonNull(analyzedDirectFlight)) {
            foundFlights.add(analyzedDirectFlight);
        }
    }

    private FlightLegModel buildFlightLeg(final FlightFilterModel filter, final FlightDetailsDto flightDetailsDto) {
        LocalDateTime scheduleDepartureTime = LocalDateTime
                .of(filter.getDepartureDateTime().toLocalDate(), flightDetailsDto.getDepartureTime());
        LocalDateTime scheduleArrivalTime = LocalDateTime
                .of(filter.getArrivalDateTime().toLocalDate(), flightDetailsDto.getArrivalTime());

        return FlightLegModel.builder()
                .departureDateTime(scheduleDepartureTime)
                .arrivalDateTime(scheduleArrivalTime)
                .departureAirport(filter.getDeparture())
                .arrivalAirport(filter.getArrival())
                .build();
    }

    private boolean dtoHasTwoLegs(final FlightPairDto flightPairDto) {
        return Objects.nonNull(flightPairDto.getFirstLeg()) && Objects.nonNull(flightPairDto.getSecondLeg());
    }

    private List<FlightPairDto> getFilteredRoutes(FlightFilterModel filter, List<AvailableRoutsDto> allAvailableRoutes) {
        final List<FlightPairDto> flightPairDtoList = new ArrayList<>();
        for (AvailableRoutsDto dto : allAvailableRoutes) {
            //direct
            if ((departuresIsEqual(filter, dto) && arrivalsIsEqual(filter, dto))) {
                if (isNullOrEmpty(dto.getConnectingAirport())) {
                    flightPairDtoList.add(FlightPairDto.builder()
                            .firstLeg(dto)
                            .departureDateTime(filter.getDepartureDateTime())
                            .arrivalDateTime(filter.getArrivalDateTime())
                            .build());
                } else if (!isNullOrEmpty(dto.getConnectingAirport())) { //TODO check maybe remove with "else"
                    AvailableRoutsDto firstLeg = AvailableRoutsDto.builder()
                            .airportFrom(dto.getAirportFrom())
                            .airportTo(dto.getConnectingAirport())
                            .operator(dto.getOperator())
                            .build();
                    AvailableRoutsDto secondLeg = AvailableRoutsDto.builder()
                            .airportFrom(dto.getConnectingAirport())
                            .airportTo(dto.getAirportTo())
                            .operator(dto.getOperator())
                            .build();

                    flightPairDtoList.add(FlightPairDto.builder()
                            .firstLeg(firstLeg)
                            .secondLeg(secondLeg)
                            .departureDateTime(filter.getDepartureDateTime())
                            .arrivalDateTime(filter.getArrivalDateTime())
                            .build());
                }
            } else if ((departuresIsEqual(filter, dto) && !arrivalsIsEqual(filter, dto) && isNullOrEmpty(dto.getConnectingAirport()))) {
                addToListIfPairExist(allAvailableRoutes, dto.getAirportTo(), filter, dto, flightPairDtoList);
            }
        }
        return flightPairDtoList;
    }

    private void addToListIfPairExist(List<AvailableRoutsDto> list, String departure, FlightFilterModel filter, AvailableRoutsDto firstLegDto, List<FlightPairDto> flightPairDtoList) {
        for (AvailableRoutsDto secondLegDto : list) {
            if (departuresIsEqual(secondLegDto.getAirportFrom(), departure)
                    && arrivalsIsEqual(secondLegDto.getAirportTo(), filter.getArrival())
                    && isNullOrEmpty(secondLegDto.getConnectingAirport())) {
                flightPairDtoList.add(
                        FlightPairDto.builder()
                                .firstLeg(firstLegDto)
                                .secondLeg(secondLegDto)
                                .departureDateTime(filter.getDepartureDateTime())
                                .arrivalDateTime(filter.getArrivalDateTime())
                                .build());
            }
        }
    }

    private List<AvailableRoutsDto> getInitiallyFilteredRoutes(FlightFilterModel filter) {
        return flightsInfoDao.getAllAvailableRoutes().stream()
                .filter(route -> departuresIsEqual(filter, route) || arrivalsIsEqual(filter, route))
                .filter(route -> !StringUtils.isEmpty(route.getOperator()) && isRyanairOperator(route.getOperator()))
                .collect(Collectors.toList());
    }

    private boolean isRyanairOperator(String operatorName) {
        return operatorName.equalsIgnoreCase(CommonConstants.RYANAIR);
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
