package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.TestDataGenerator;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.InterconnectingFlightsService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.List;

public class AvailableFlightsControllerTest {
    private InterconnectingFlightsService mockService = Mockito.mock(InterconnectingFlightsService.class);
    private AvailableFlightsController controller = new AvailableFlightsController(mockService);

    @Test
    public void shouldReturnExpectedList() {
        //Given
        final FlightFilterModel filter = TestDataGenerator.getWroDubFlightFilter();
        final List<FoundFlightsModel> expectedResult = TestDataGenerator.getListOfFoundFlights();

        //When
        Mockito.when(mockService.getAppropriateFlights(filter))
                .thenReturn(expectedResult);

        final ResponseEntity<List<FoundFlightsModel>> actualResult = controller
                .getAllAvailableFlightSchedules(filter);

        //Then
        Assertions.assertThat(actualResult.getBody()).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnSuccessStatusCode() {
        //Given
        final FlightFilterModel filter = TestDataGenerator.getWroDubFlightFilter();
        final List<FoundFlightsModel> expectedResult = TestDataGenerator.getListOfFoundFlights();

        //When
        Mockito.when(mockService.getAppropriateFlights(filter))
                .thenReturn(expectedResult);

        final ResponseEntity<List<FoundFlightsModel>> actualResult = controller
                .getAllAvailableFlightSchedules(filter);

        //Then
        Assertions.assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test(expected = RestClientException.class)
    public void shouldReturnInternalServerErrorStatusCode() {
        //Given
        final AvailableFlightsController mockController = Mockito.mock(AvailableFlightsController.class);
        final FlightFilterModel filter = TestDataGenerator.getWroDubFlightFilter();

        //When
        Mockito.when(mockController.getAllAvailableFlightSchedules(filter)).thenThrow(RestClientException.class);

        mockController.getAllAvailableFlightSchedules(filter);
        //Then
        //Should throw RestClientException exception
    }
}