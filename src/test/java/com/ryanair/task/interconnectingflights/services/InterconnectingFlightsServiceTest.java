package com.ryanair.task.interconnectingflights.services;


import com.ryanair.task.interconnectingflights.TestDataProvider;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.analyzers.FlightScheduleAnalyzer;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class InterconnectingFlightsServiceTest {
    private FlightScheduleAnalyzer analyzer = Mockito.mock(FlightScheduleAnalyzer.class);
    private final InterconnectingFlightsService service = new InterconnectingFlightsService(analyzer);

    @Test
    public void shouldReturnListFlightModels() {
        //Given
        final FlightFilterModel filter = TestDataProvider.getDubWroFlightFilter();
        List<FoundFlightsModel> expectedResult = TestDataProvider.getListOfFoundFlights();
        Mockito.when(analyzer.analyzeAndGetFinalListOfFlights(filter)).thenReturn(expectedResult);

        //When
        List<FoundFlightsModel> actualResult = service.getAppropriateFlights(filter);

        //Then
        Assertions.assertThat(actualResult).isEqualTo(expectedResult);
    }
}