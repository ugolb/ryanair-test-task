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

    /**
     * Method build available flight rotes.
     *
     * @param airportFrom - departure airport IATA code.
     * @param airportTo   - arrival airport IATA code.
     * @param operator    - flight operator.
     * @return AvailableRoutsDto object.
     */
    protected AvailableRoutsDto buildAvailableRoutsDto(String airportFrom, String airportTo, String operator) {
        return AvailableRoutsDto.builder()
                .airportFrom(airportFrom)
                .airportTo(airportTo)
                .operator(operator)
                .build();
    }

    /**
     * Method filters list of all available routes based on filter received from request.
     *
     * @param filter                - request filter.
     * @param availableRoutsDtoList - list of dto's which should be filtered .
     * @return List of appropriate routes.
     */
    protected List<AvailableRoutsDto> getInitiallyFilteredRoutes(final FlightFilterModel filter,
                                                                 List<AvailableRoutsDto> availableRoutsDtoList) {
        return availableRoutsDtoList.stream()
                .filter(route -> ApplicationUtils.departuresIsEqual(filter, route)
                        || ApplicationUtils.arrivalsIsEqual(filter, route))
                .filter(route -> !StringUtils.isEmpty(route.getOperator())
                        && ApplicationUtils.isRyanairOperator(route.getOperator()))
                .collect(Collectors.toList());
    }

    /**
     * Method creates FlightPairDto object which will contain firstLeg and secondLeg in case if flight interconnected
     * and returns firstLeg and secondLEg == null
     *
     * @param filter             - filter received from request.
     * @param allAvailableRoutes - initially filtered available routes.
     * @return list of FlightPairDto.
     */
    protected List<FlightPairDto> getFlightPairs(FlightFilterModel filter, List<AvailableRoutsDto> allAvailableRoutes) {
        final List<FlightPairDto> flightPairDtoList = new ArrayList<>();
        for (AvailableRoutsDto dto : getInitiallyFilteredRoutes(filter, allAvailableRoutes)) {
            if ((ApplicationUtils.departuresIsEqual(filter, dto) && ApplicationUtils.arrivalsIsEqual(filter, dto))) {
                if (StringUtils.isEmpty(dto.getConnectingAirport())) {
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
                    && StringUtils.isEmpty(dto.getConnectingAirport()))) {
                ApplicationUtils.addToListIfPairExist(allAvailableRoutes, dto.getAirportTo(), filter, dto,
                        flightPairDtoList);
            }
        }
        return flightPairDtoList;
    }

    /**
     * Method analyze interconnected flights, look for appropriate pair combination based on:
     * First departure dateTime must be more or equal then departure time from filter;
     * Last arrival dateTime must be less or equal then arrival time in filter;
     * Time difference between first arrival and second departure must be more or equal then two hours.
     *
     * @param availableScheduleFirstLeg  - available schedules for first flight.
     * @param availableScheduleSecondLeg - available schedules for second flight.
     * @param flightPairDto              - all available routes.
     * @param filter                     - filter from request.
     * @return list of FoundFlightsModel objects.
     */
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


    /**
     * Returns true if time difference between two flights >= CommonConstants.DIFF_BETWEEN_FLIGHTS otherwise false.
     *
     * @param firstFlight  - first flight from interconnecting chain.
     * @param secondFlight - second flight from interconnecting chain.
     * @return true / false
     */
    private boolean isProperDiffTime(FlightDetailsDto firstFlight, FlightDetailsDto secondFlight) {
        return ChronoUnit.HOURS.between(firstFlight.getArrivalTime(),
                secondFlight.getDepartureTime()) >= CommonConstants.DIFF_BETWEEN_FLIGHTS;
    }

    /**
     * Method analyze direct flights, look for appropriate pair combination based on:
     * Departure dateTime must be more or equal then departure time from filter;
     * Arrival dateTime must be less or equal then arrival time in filter;
     *
     * @param schedulesDto  - available schedules for first flight.
     * @param flightPairDto - all available routes.
     * @param filter        - filter from request.
     * @return list of FoundFlightsModel objects.
     */
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

    /**
     * Checks if current flight is direct or interconnected.
     *
     * @return true if flight is interconnected and false otherwise.
     */
    protected boolean isInterconnectingFlight(final FlightPairDto flightPairDto) {
        return Objects.nonNull(flightPairDto.getFirstLeg()) && Objects.nonNull(flightPairDto.getSecondLeg());
    }
}
