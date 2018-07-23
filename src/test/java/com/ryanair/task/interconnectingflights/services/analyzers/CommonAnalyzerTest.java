package com.ryanair.task.interconnectingflights.services.analyzers;

import com.ryanair.task.interconnectingflights.TestDataProvider;
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
        List<FlightPairDto> expectedResult = TestDataProvider.getExpectedFlightPairDto();

        //When
        List<FlightPairDto> actualResult = commonAnalyzer.getFlightPairs(
                TestDataProvider.getDubWroFlightFilter(), TestDataProvider.getListOfAvailableRoutsDto());

        //Then
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnEmptyListIfDoesNotFoundAppropriateRoute() {
        //Given
        List<FlightPairDto> expectedResult = Collections.emptyList();

        //When
        List<FlightPairDto> actualResult = commonAnalyzer.getFlightPairs(
                TestDataProvider.getNotExistingRouteFlightFilter(), TestDataProvider.getListOfAvailableRoutsDto());

        //Then
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    public void shouldProperlyFilterDirectFlights() {
        //Given
        final FoundFlightsModel expectedResult = FoundFlightsModel.builder()
                .legs(Collections.singletonList(TestDataProvider.getFlightLegsModel()))
                .stops(CommonConstants.WITHOUT_STOPS)
                .build();

        //When
        final List<FoundFlightsModel> actualResult = commonAnalyzer.getAnalyzedDirectFlight(
                TestDataProvider.getAvailableSchedulesDtoFl(), TestDataProvider.getDirectRout(),
                TestDataProvider.getDubWroFlightFilter());

        //Then
        Assertions.assertThat(actualResult.get(0)).isEqualTo(expectedResult);
    }

    @Test
    public void shouldProperlyFilterFlightsWithConnectionAirport() {
        //Given
        final FoundFlightsModel expectedResult = FoundFlightsModel.builder()
                .legs(Arrays.asList(TestDataProvider.getDubWawModel(), TestDataProvider.getWawDubModel()))
                .stops(CommonConstants.ONE_STOP)
                .build();

        //When
        final List<FoundFlightsModel> actualResult = commonAnalyzer.getAnalyzedInterconnectedFlight(
                TestDataProvider.getAvailableSchedulesDtoFl(), TestDataProvider.getAvailableSchedulesDtoSl(),
                TestDataProvider.getRoutesWithConnectionAirport(), TestDataProvider.getDubWroFlightFilter());

        //Then
        Assertions.assertThat(actualResult.get(0)).isEqualTo(expectedResult);
    }
}