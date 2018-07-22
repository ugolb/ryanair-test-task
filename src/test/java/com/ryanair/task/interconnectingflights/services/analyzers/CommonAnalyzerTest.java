package com.ryanair.task.interconnectingflights.services.analyzers;

import com.ryanair.task.interconnectingflights.TestDataGenerator;
import com.ryanair.task.interconnectingflights.constants.CommonConstants;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.dtos.FlightPairDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonAnalyzerTest {
    private final CommonAnalyzer commonAnalyzer = new CommonAnalyzer();

    @Test
    public void shouldProperlyFilterAllRoutes() {
        //Given
        List<FlightPairDto> expectedResult = TestDataGenerator.getExpectedFlightPairDto();

        //When
        List<FlightPairDto> actualResult = commonAnalyzer.getFlightPairs(
                TestDataGenerator.getDubWroFlightFilter(), TestDataGenerator.getListOfAvailableRoutsDto());

        //Then
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnEmptyListIfDoesNotFoundAppropriateRoute() {
        //Given
        List<FlightPairDto> expectedResult = Collections.emptyList();

        //When
        List<FlightPairDto> actualResult = commonAnalyzer.getFlightPairs(
                TestDataGenerator.getNotExistingRouteFlightFilter(), TestDataGenerator.getListOfAvailableRoutsDto());

        //Then
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void shouldProperlyFilterDirectFlights() {
        //Given
        final FoundFlightsModel expectedResult = FoundFlightsModel.builder()
                .legs(Collections.singletonList(TestDataGenerator.getFlightLegsModel()))
                .stops(CommonConstants.WITHOUT_STOPS)
                .build();

        //When
        final List<FoundFlightsModel> actualResult = commonAnalyzer.getAnalyzedDirectFlight(
                TestDataGenerator.getAvailableSchedulesDtoFl(), TestDataGenerator.getDirectRout(),
                TestDataGenerator.getDubWroFlightFilter());

        //Then
        Assertions.assertThat(actualResult.get(0)).isEqualTo(expectedResult);
    }

    @Test
    public void shouldProperlyFilterFlightsWithConnectionAirport() {
        //Given
        final FoundFlightsModel expectedResult = FoundFlightsModel.builder()
                .legs(Arrays.asList(TestDataGenerator.getDubWawModel(), TestDataGenerator.getWawDubModel()))
                .stops(CommonConstants.ONE_STOP)
                .build();

        //When
        final List<FoundFlightsModel> actualResult = commonAnalyzer.getAnalyzedInterconnectedFlight(
                TestDataGenerator.getAvailableSchedulesDtoFl(), TestDataGenerator.getAvailableSchedulesDtoSl(),
                TestDataGenerator.getRoutesWithConnectionAirport(), TestDataGenerator.getDubWroFlightFilter());

        //Then
        Assertions.assertThat(actualResult.get(0)).isEqualTo(expectedResult);
    }
}