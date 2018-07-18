package com.ryanair.task.interconnectingflights.clients;

import com.ryanair.task.interconnectingflights.constants.RestClientConstants;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableSchedulesDto;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

public class RestClientCommonTest {
    private static final String TEST_URL = "https://api.ryanair.com/timetable/3/schedules/DUB/WRO/years/2018/months/7";
    private static final String TEST_URL_WITHOUT_ALLOWED_SCHEDULE = "https://api.ryanair.com/timetable/3/schedules" +
            "/DUB/WRO/years/1900/months/7";

    final RestClientCommon mockRestClientCommon = Mockito.mock(RestClientCommon.class);

    @Test
    public void shouldSuccessfullyReturnNotEmptyList() {
        //Given
        RestClientCommon restClientCommon = new RestClientCommon();


        //When
        List<AvailableRoutsDto> actualResult = restClientCommon.sendGetRequestForListOfObjects(
                new ParameterizedTypeReference<List<AvailableRoutsDto>>() {
                }, RestClientConstants.ROUTES_API_LINK);
        System.out.println(actualResult.size());
        //Then
        Assertions.assertThat(actualResult).isNotEmpty();
    }

    @Test
    public void shouldSuccessfullyReturnAvailableSchedules() {
        //Given
        RestClientCommon restClientCommon = new RestClientCommon();

        //When
        AvailableSchedulesDto actualResult = restClientCommon.sendGetRequestForOneObject(
                new ParameterizedTypeReference<AvailableSchedulesDto>() {
                }, TEST_URL);

        //Then
        Assertions.assertThat(actualResult).isNotNull();

    }

    @Test(expected = HttpClientErrorException.class)
    public void shouldThrowExceptionIfScheduleDoesNotExist() {
        //Given
        RestClientCommon restClientCommon = new RestClientCommon();

        //When
        AvailableSchedulesDto actualResult = restClientCommon.sendGetRequestForOneObject(
                new ParameterizedTypeReference<AvailableSchedulesDto>() {
                }, TEST_URL_WITHOUT_ALLOWED_SCHEDULE);

        //Then
        Assertions.assertThat(actualResult).isNotNull();

    }
}