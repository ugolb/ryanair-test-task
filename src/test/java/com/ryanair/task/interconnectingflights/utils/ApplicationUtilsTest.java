package com.ryanair.task.interconnectingflights.utils;

import com.ryanair.task.interconnectingflights.TestDataGenerator;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;


public class ApplicationUtilsTest {
    private final FlightFilterModel filterModel = new FlightFilterModel("DUB", "WRO", "2018-07-01T07:00:00", "2018-07-01T07:00:00");
    private final String TEST_URL = "https://api.ryanair.com/timetable/3/schedules/WRO/DUB/years/2018/months/7";

    @Test
    public void shouldConstructProperUrl() {
        //When
        final String actualResult = ApplicationUtils
                .buildScheduleServiceUrl(TestDataGenerator.IATA_WRO, TestDataGenerator.IATA_DUB, filterModel);

        //Then
        Assertions.assertThat(actualResult).isEqualTo(TEST_URL);

    }
}