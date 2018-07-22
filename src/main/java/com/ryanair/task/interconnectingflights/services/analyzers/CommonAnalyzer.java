package com.ryanair.task.interconnectingflights.services.analyzers;

import com.ryanair.task.interconnectingflights.constants.CommonConstants;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FlightLegModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableSchedulesDto;
import com.ryanair.task.interconnectingflights.services.dtos.FlightDetailsDto;
import com.ryanair.task.interconnectingflights.services.dtos.FlightPairDto;
import com.ryanair.task.interconnectingflights.utils.ApplicationUtils;
import com.ryanair.task.interconnectingflights.utils.DateTimeUtil;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class CommonAnalyzer {

    protected AvailableRoutsDto buildAvailableRoutsDto(String airportFrom, String airportTo, String operator) {
        return AvailableRoutsDto.builder()
                .airportFrom(airportFrom)
                .airportTo(airportTo)
                .operator(operator)
                .build();
    }

    protected List<AvailableRoutsDto> getInitiallyFilteredRoutes(final FlightFilterModel filter,
                                                                 List<AvailableRoutsDto> flightsInfoDao) {
        return flightsInfoDao.stream()
                .filter(route -> ApplicationUtils.departuresIsEqual(filter, route)
                        || ApplicationUtils.arrivalsIsEqual(filter, route))
                .filter(route -> !StringUtils.isEmpty(route.getOperator())
                        && ApplicationUtils.isRyanairOperator(route.getOperator()))
                .collect(Collectors.toList());
    }

    protected List<FlightPairDto> getFilteredRoutes(FlightFilterModel filter, List<AvailableRoutsDto> allAvailableRoutes) {
        final List<FlightPairDto> flightPairDtoList = new ArrayList<>();
        for (AvailableRoutsDto dto : getInitiallyFilteredRoutes(filter, allAvailableRoutes)) {
            if ((ApplicationUtils.departuresIsEqual(filter, dto) && ApplicationUtils.arrivalsIsEqual(filter, dto))) {
                if (ApplicationUtils.isNullOrEmpty(dto.getConnectingAirport())) {
                    ApplicationUtils.addToFlightPairList(flightPairDtoList, dto, null, filter);
                } else {
                    AvailableRoutsDto firstLeg = buildAvailableRoutsDto(dto.getAirportFrom(),
                            dto.getConnectingAirport(), dto.getOperator());
                    AvailableRoutsDto secondLeg = buildAvailableRoutsDto(dto.getConnectingAirport(),
                            dto.getAirportTo(), dto.getOperator());

                    ApplicationUtils.addToFlightPairList(flightPairDtoList, firstLeg, secondLeg, filter);
                }
            } else if ((ApplicationUtils.departuresIsEqual(filter, dto)
                    && !ApplicationUtils.arrivalsIsEqual(filter, dto)
                    && ApplicationUtils.isNullOrEmpty(dto.getConnectingAirport()))) {
                ApplicationUtils.addToListIfPairExist(allAvailableRoutes, dto.getAirportTo(), filter, dto,
                        flightPairDtoList);
            }
        }
        return flightPairDtoList;
    }

    protected List<FoundFlightsModel> getAnalyzedInterconnectedFlight(final AvailableSchedulesDto availableScheduleFirstLeg,
                                                                      final AvailableSchedulesDto availableScheduleSecondLeg,
                                                                      final FlightPairDto flightPairDto,
                                                                      final FlightFilterModel filter) {
        if (Objects.isNull(availableScheduleFirstLeg) || Objects.isNull(availableScheduleSecondLeg)) {
            return Collections.emptyList();
        }

        final List<FlightDetailsDto> flightScheduleFirstLeg = ApplicationUtils.getScheduleByDay(availableScheduleFirstLeg,
                flightPairDto.getDepartureDateTime());
        final List<FlightDetailsDto> flightScheduleSecondLeg = ApplicationUtils.getScheduleByDay(availableScheduleSecondLeg,
                flightPairDto.getArrivalDateTime());

        List<FoundFlightsModel> listOfFoundFlightsModel = new ArrayList<>();

        for (FlightDetailsDto currentFlight : flightScheduleFirstLeg) {
            for (FlightDetailsDto secondLegSchedule : flightScheduleSecondLeg) {
                if (isProperDiffTime(currentFlight, secondLegSchedule)
                        && DateTimeUtil.isInRange(filter, currentFlight, secondLegSchedule)) {
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

    private boolean isProperDiffTime(FlightDetailsDto currentFlight, FlightDetailsDto secondLegSchedule) {
        return ChronoUnit.HOURS.between(currentFlight.getArrivalTime(),
                secondLegSchedule.getDepartureTime()) >= CommonConstants.DIFF_BETWEEN_FLIGHTS;
    }

    protected List<FoundFlightsModel> getAnalyzedDirectFlight(final AvailableSchedulesDto schedulesDto,
                                                              final FlightPairDto flightPairDto,
                                                              final FlightFilterModel filter) {
        if (Objects.isNull(schedulesDto)) {
            return Collections.emptyList();
        }

        final List<FlightDetailsDto> schedulesForParticularDate = ApplicationUtils.getScheduleByDay(schedulesDto,
                flightPairDto.getDepartureDateTime());

        for (FlightDetailsDto currentFlight : schedulesForParticularDate) {
            if (DateTimeUtil.isInRange(currentFlight, flightPairDto)) {
                final FlightLegModel flightLegModel = ApplicationUtils.buildFlightLeg(filter, currentFlight);
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

    protected boolean dtoHasTwoLegs(final FlightPairDto flightPairDto) {
        return Objects.nonNull(flightPairDto.getFirstLeg()) && Objects.nonNull(flightPairDto.getSecondLeg());
    }
}
