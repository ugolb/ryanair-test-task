package com.ryanair.task.interconnectingflights.controller;

import com.ryanair.task.interconnectingflights.TestDataGenerator;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.InterconnectingFlightsService;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RunWith(JUnitParamsRunner.class)
public class AvailableFlightsControllerTest {
    private InterconnectingFlightsService mockService = Mockito.mock(InterconnectingFlightsService.class);
    private AvailableFlightsController controller = new AvailableFlightsController(mockService);
    private static final String TEST_VALUE = "TEST_VALUE";
    private static final String TEST_TIME = "2016-03-01T07:00:00";

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

    @Test(expected = IllegalArgumentException.class)
    @Parameters(method = "parametersToTestAdd")
    public void shouldThrowExceptionIfAnyOfInputParamsNullOrEmpty(String departure, String arrival,
                                                                  String departureDateTime, String arrivalDateTime) {
        //When
        new FlightFilterModel(departure, arrival, departureDateTime, arrivalDateTime);

        //Then
        //IllegalArgumentException should be thrown

    }

    private Object[] parametersToTestAdd() {
        return new Object[]{
                new Object[]{"", TEST_VALUE, TEST_TIME, TEST_TIME},
                new Object[]{TEST_VALUE, "", TEST_TIME, TEST_TIME},
                new Object[]{TEST_VALUE, TEST_VALUE, "", TEST_TIME},
                new Object[]{TEST_VALUE, TEST_VALUE, TEST_TIME, ""},
                new Object[]{null, TEST_VALUE, TEST_TIME, TEST_TIME},
                new Object[]{TEST_VALUE, null, TEST_TIME, TEST_TIME},
                new Object[]{TEST_VALUE, TEST_VALUE, null, TEST_TIME},
                new Object[]{TEST_VALUE, TEST_VALUE, TEST_TIME, null},
        };
    }
}