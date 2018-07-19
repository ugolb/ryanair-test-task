package com.ryanair.task.interconnectingflights;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public final class TestDataGenerator {
    public static final String IATA_WRO = "WRO";
    public static final String IATA_DUB = "DUB";
    public static final String IATA_WAW = "WAW";
    public static final String IATA_ZAZ = "ZAZ";
    public static final String IATA_ACE = "ACE";
    public static final String IATA_LTN = "LTN";
    public static final String TEST_TIME = "2016-03-01T07:00:00";
    public static final LocalDateTime CURRENT_TIME = LocalDateTime.now();

    public static List<AvailableRoutsDto> getListOfAvailableRoutsDto() {
        return Arrays.asList(
                AvailableRoutsDto.builder().airportFrom(IATA_WRO).airportTo(IATA_DUB).build(), //Y 1L

                AvailableRoutsDto.builder().airportFrom(IATA_WRO).airportTo(IATA_ZAZ).build(), //Y 1L
                AvailableRoutsDto.builder().airportFrom(IATA_ZAZ).airportTo(IATA_DUB).build(), //Y 1L

                AvailableRoutsDto.builder().airportFrom(IATA_WRO).airportTo(IATA_ACE).build(), //Y 1L
                AvailableRoutsDto.builder().airportFrom(IATA_ACE).airportTo(IATA_DUB).build(), //Y 1L

                AvailableRoutsDto.builder().airportFrom(IATA_WRO).airportTo(IATA_DUB).connectingAirport(IATA_WAW).build(),//Y 2L

                AvailableRoutsDto.builder().airportFrom(IATA_ZAZ).airportTo(IATA_DUB).connectingAirport(IATA_WAW).build(),//N
                AvailableRoutsDto.builder().airportFrom(IATA_WRO).airportTo(IATA_ZAZ).connectingAirport(IATA_WAW).build(),//N
                AvailableRoutsDto.builder().airportFrom(IATA_ACE).airportTo(IATA_LTN).connectingAirport(IATA_WAW).build()//N
        );
    }

    public static FlightFilterModel getWroBubFlightFilter() {
        return new FlightFilterModel(IATA_WRO, IATA_DUB, TEST_TIME, TEST_TIME);
    }

    private TestDataGenerator() {
    }
}
