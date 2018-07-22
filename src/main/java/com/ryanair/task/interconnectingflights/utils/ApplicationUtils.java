package com.ryanair.task.interconnectingflights.utils;

import com.ryanair.task.interconnectingflights.constants.CommonConstants;
import com.ryanair.task.interconnectingflights.constants.RestClientConstants;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FlightLegModel;
import com.ryanair.task.interconnectingflights.services.dtos.*;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Common application utilities.
 */
public final class ApplicationUtils {

    /**
     * Private constructor to avoid util class instantiation
     */
    private ApplicationUtils() {
    }

    /**
     * Builds link for ScheduleService based on filter received in request.
     *
     * @param departure - departure airport.
     * @param arrival   - arrival airport.
     * @param filter    - filter received in request.
     * @return String which is built URL.
     */
    public static String buildScheduleServiceUrl(String departure, String arrival, final FlightFilterModel filter) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("departure", departure);
        parameters.put("arrival", arrival);
        parameters.put("year", DateTimeUtil.getYearInString(filter.getDepartureDateTime()));
        parameters.put("month", DateTimeUtil.getMonthInString(filter.getDepartureDateTime()));

        return UriComponentsBuilder.fromHttpUrl(RestClientConstants.SCHEDULES_API_LINK).build(parameters).toString();
    }

    /**
     * Method filters schedules list based on month day.
     *
     * @param schedulesDto - schedules which should be filtered.
     * @param dateTime     - date and time based on which schedules will be filtered.
     * @return list of FlightDetailsDto objects.
     */
    public static List<FlightDetailsDto> getScheduleByDay(final AvailableSchedulesDto schedulesDto,
                                                          final LocalDateTime dateTime) {
        List<ScheduleDayDto> days = schedulesDto.getDays();
        for (ScheduleDayDto currentSchedule : days) {
            if (currentSchedule.getDay() == dateTime.getDayOfMonth()) {
                return currentSchedule.getFlights();
            }
        }
        return Collections.emptyList();
    }

    /**
     * Method build flight leg.
     *
     * @return FlightLegModel object.
     */
    public static FlightLegModel buildFlightLeg(final FlightFilterModel filter, final FlightDetailsDto flightDetailsDto) {
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

    /**
     * Method adds dto to the list if pair exist.
     */
    public static void addToListIfPairExist(List<AvailableRoutsDto> list, String departure, FlightFilterModel filter, AvailableRoutsDto firstLegDto, List<FlightPairDto> flightPairDtoList) {
        for (AvailableRoutsDto secondLegDto : list) {
            if (secondLegDto.getAirportFrom().equals(departure)
                    && secondLegDto.getAirportTo().equals(filter.getArrival())
                    && StringUtils.isEmpty(secondLegDto.getConnectingAirport())) {
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

    /**
     * Method add flight pair dto to the list.
     */
    public static void addToFlightPairList(final List<FlightPairDto> flightPairDtoList, final AvailableRoutsDto firstLeg,
                                           final AvailableRoutsDto secondLeg, final FlightFilterModel filter) {
        flightPairDtoList.add(FlightPairDto.builder()
                .firstLeg(firstLeg)
                .secondLeg(secondLeg)
                .departureDateTime(filter.getDepartureDateTime())
                .arrivalDateTime(filter.getArrivalDateTime())
                .build());
    }

    /**
     * Return true if flight operator id Ryanair otherwise false.
     *
     * @param operatorName - value need to be checked.
     * @return true / false
     */
    public static boolean isRyanairOperator(String operatorName) {
        return operatorName.equalsIgnoreCase(CommonConstants.RYANAIR);
    }

    /**
     * Return true if passed dates are equal otherwise false.
     *
     * @return true / false
     */
    public static boolean departuresIsEqual(FlightFilterModel filter, AvailableRoutsDto route) {
        return route.getAirportFrom().equals(filter.getDeparture());
    }

    /**
     * Return true if passed dates are equal otherwise false.
     *
     * @return true / false
     */
    public static boolean arrivalsIsEqual(FlightFilterModel filter, AvailableRoutsDto route) {
        return route.getAirportTo().equals(filter.getArrival());
    }
}
