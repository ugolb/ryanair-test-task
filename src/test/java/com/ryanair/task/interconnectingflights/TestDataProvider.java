package com.ryanair.task.interconnectingflights;

import com.ryanair.task.interconnectingflights.constants.CommonConstants;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FlightLegModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.dtos.*;
import com.ryanair.task.interconnectingflights.utils.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TestDataProvider {
    public static final String IATA_WRO = "WRO";
    public static final String IATA_DUB = "DUB";
    private static final String IATA_WAW = "WAW";
    private static final String IATA_ZAZ = "ZAZ";
    private static final String IATA_ACE = "ACE";
    private static final String IATA_LTN = "LTN";
    private static final String TEST_TIME = "2016-03-01T07:00:00";
    private static final String FIRST_LEG_DEP_DATETIME = "2018-07-22T10:19:00";
    private static final String FIRST_LEG_ARR_DATETIME = "2018-07-22T18:20:00";

    private static final LocalTime DEPARTURE_TIME_IN_RANGE = LocalTime.of(10, 20);
    private static final LocalTime ARRIVAL_TIME_IN_RANGE = LocalTime.of(11, 20);
    private static final LocalTime DEPARTURE_TIME_OUT_OF_RANGE = LocalTime.of(9, 20);
    private static final LocalTime ARRIVAL_TIME_IN_OUT_OF_RANGE = LocalTime.of(12, 20);
    private static final LocalDate DATE_IN_RANGE = LocalDate.of(2018, 7, 22);
    private static final String NOT_RYANAIR = "NOT_RYANAIR";


    public static List<AvailableRoutsDto> getListOfAvailableRoutsDto() {
        return Arrays.asList(
                //Direct route
                AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).build(),
                //Interconnected without specified connecting airport
                AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_ACE).operator(CommonConstants.RYANAIR).build(),
                AvailableRoutsDto.builder().airportFrom(IATA_ACE).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).build(),
                //Interconnected with specified connecting airport
                AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).connectingAirport(IATA_WAW).build(),
                //Should be excluded during routes filtration
                AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WRO).operator(NOT_RYANAIR).build(),
                AvailableRoutsDto.builder().airportFrom(IATA_ACE).airportTo(IATA_LTN).operator(CommonConstants.RYANAIR).connectingAirport(IATA_WAW).build(),
                AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WAW).operator(CommonConstants.RYANAIR).connectingAirport(IATA_LTN).build(),
                AvailableRoutsDto.builder().airportFrom(IATA_WAW).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).connectingAirport(IATA_ZAZ).build()
        );
    }

    public static List<FlightPairDto> getExpectedFlightPairDto() {
        return Arrays.asList(
                FlightPairDto.builder()
                        .firstLeg(AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).build())
                        .departureDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_DEP_DATETIME))
                        .arrivalDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_ARR_DATETIME))
                        .build(),
                FlightPairDto.builder()
                        .firstLeg(AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_ACE).operator(CommonConstants.RYANAIR).build())
                        .secondLeg(AvailableRoutsDto.builder().airportFrom(IATA_ACE).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).build())
                        .departureDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_DEP_DATETIME))
                        .arrivalDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_ARR_DATETIME))
                        .build(),
                FlightPairDto.builder()
                        .firstLeg(AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WAW).operator(CommonConstants.RYANAIR).build())
                        .secondLeg(AvailableRoutsDto.builder().airportFrom(IATA_WAW).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).build())
                        .departureDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_DEP_DATETIME))
                        .arrivalDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_ARR_DATETIME))
                        .build()
        );
    }

    public static FlightFilterModel getWroDubFlightFilter() {
        return new FlightFilterModel(IATA_WRO, IATA_DUB, TEST_TIME, TEST_TIME);
    }

    public static FlightFilterModel getDubWroFlightFilter() {
        return new FlightFilterModel(IATA_DUB, IATA_WRO, FIRST_LEG_DEP_DATETIME, FIRST_LEG_ARR_DATETIME);
    }

    public static FlightFilterModel getNotExistingRouteFlightFilter() {
        return new FlightFilterModel(IATA_LTN, IATA_ACE, FIRST_LEG_DEP_DATETIME, FIRST_LEG_ARR_DATETIME);
    }

    public static List<FoundFlightsModel> getListOfFoundFlights() {
        return Collections.singletonList(
                FoundFlightsModel.builder()
                        .stops(CommonConstants.WITHOUT_STOPS)
                        .legs(Collections.singletonList(getFlightLegsModel()))
                        .build()
        );
    }

    public static FlightLegModel getFlightLegsModel() {
        return FlightLegModel.builder()
                .departureDateTime(LocalDateTime.of(DATE_IN_RANGE, DEPARTURE_TIME_IN_RANGE))
                .arrivalDateTime(LocalDateTime.of(DATE_IN_RANGE, ARRIVAL_TIME_IN_RANGE))
                .departureAirport(IATA_DUB)
                .arrivalAirport(IATA_WRO)
                .build();
    }

    public static FlightLegModel getDubWawModel() {
        return FlightLegModel.builder()
                .departureDateTime(LocalDateTime.of(DATE_IN_RANGE, DEPARTURE_TIME_IN_RANGE))
                .arrivalDateTime(LocalDateTime.of(DATE_IN_RANGE, ARRIVAL_TIME_IN_RANGE))
                .departureAirport(IATA_DUB)
                .arrivalAirport(IATA_WAW)
                .build();
    }

    public static FlightLegModel getWawDubModel() {
        return FlightLegModel.builder()
                .departureDateTime(LocalDateTime.of(DATE_IN_RANGE, DEPARTURE_TIME_IN_RANGE).plusHours(4))
                .arrivalDateTime(LocalDateTime.of(DATE_IN_RANGE, ARRIVAL_TIME_IN_RANGE).plusHours(5))
                .departureAirport(IATA_WAW)
                .arrivalAirport(IATA_WRO)
                .build();
    }

    public static AvailableSchedulesDto getAvailableSchedulesDtoFl() {
        return AvailableSchedulesDto.builder()
                .month(7)
                .days(getDaysFl())
                .build();
    }

    private static List<ScheduleDayDto> getDaysFl() {
        return Arrays.asList(
                ScheduleDayDto.builder()
                        .day(21)
                        .flights(getListOfFlightDetailsFl())
                        .build(),
                ScheduleDayDto.builder()
                        .day(22)
                        .flights(getListOfFlightDetailsFl())
                        .build()
        );
    }

    private static List<FlightDetailsDto> getListOfFlightDetailsFl() {
        return Arrays.asList(
                FlightDetailsDto.builder()
                        .departureTime(DEPARTURE_TIME_IN_RANGE)
                        .arrivalTime(ARRIVAL_TIME_IN_RANGE)
                        .build(),
                FlightDetailsDto.builder()
                        .departureTime(DEPARTURE_TIME_OUT_OF_RANGE)
                        .arrivalTime(ARRIVAL_TIME_IN_OUT_OF_RANGE)
                        .build()
        );
    }

    public static AvailableSchedulesDto getAvailableSchedulesDtoSl() {
        return AvailableSchedulesDto.builder()
                .month(7)
                .days(getDaysSl())
                .build();
    }

    private static List<ScheduleDayDto> getDaysSl() {
        return Arrays.asList(
                ScheduleDayDto.builder()
                        .day(21)
                        .flights(getListOfFlightDetailsSl())
                        .build(),
                ScheduleDayDto.builder()
                        .day(22)
                        .flights(getListOfFlightDetailsSl())
                        .build()
        );
    }

    private static List<FlightDetailsDto> getListOfFlightDetailsSl() {
        return Arrays.asList(
                FlightDetailsDto.builder()
                        .departureTime(DEPARTURE_TIME_IN_RANGE.plusHours(4))
                        .arrivalTime(ARRIVAL_TIME_IN_RANGE.plusHours(5))
                        .build(),
                FlightDetailsDto.builder()
                        .departureTime(DEPARTURE_TIME_OUT_OF_RANGE)
                        .arrivalTime(ARRIVAL_TIME_IN_OUT_OF_RANGE)
                        .build()
        );
    }

    public static FlightPairDto getDirectRout() {
        return FlightPairDto.builder()
                .firstLeg(AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).build())
                .departureDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_DEP_DATETIME))
                .arrivalDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_ARR_DATETIME))
                .build();
    }

    public static FlightPairDto getRoutesWithConnectionAirport() {
        return FlightPairDto.builder()
                .firstLeg(AvailableRoutsDto.builder().airportFrom(IATA_DUB).airportTo(IATA_WAW).operator(CommonConstants.RYANAIR).build())
                .secondLeg(AvailableRoutsDto.builder().airportFrom(IATA_WAW).airportTo(IATA_WRO).operator(CommonConstants.RYANAIR).build())
                .departureDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_DEP_DATETIME))
                .arrivalDateTime(DateTimeUtil.convertStringToLocalDateTime(FIRST_LEG_ARR_DATETIME))
                .build();
    }

    private TestDataProvider() {
    }
}
