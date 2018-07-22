package com.ryanair.task.interconnectingflights.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtilTest {
    private static final String TEST_DATE_TIME = "2016-03-01T07:00:00";
    private static final String TEST_DATE_TIME_SPECIFICALLY_FORMATTED = "07:00:00 2016-03-01";
    private static final LocalDateTime TEST_LOCAL_DATE_TIME = LocalDateTime.of(2016, 3, 1, 7, 0);

    @Test
    public void shouldProperlyConvertStringToDateTime() {
        //When
        final LocalDateTime actualResult = DateTimeUtil.convertStringToLocalDateTime(TEST_DATE_TIME);

        //Then
        Assertions.assertThat(actualResult).isEqualTo(TEST_LOCAL_DATE_TIME);
    }

    @Test
    public void shouldProperlyConvertDateTimeToStringWithIsoFormat() {
        //When
        String actualResult = DateTimeUtil.convertLocalDateTimeToString(TEST_LOCAL_DATE_TIME);

        //Then
        Assertions.assertThat(actualResult).isEqualTo(TEST_DATE_TIME);
    }

    @Test
    public void shouldProperlyConvertDateTimeToStringAccordingToSpecifiedFormat() {
        //Given
        final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss yyyy-MM-dd");

        //When
        String actualResult = DateTimeUtil.convertLocalDateTimeToString(TEST_LOCAL_DATE_TIME, dateTimeFormat);

        //Then
        Assertions.assertThat(actualResult).isEqualTo(TEST_DATE_TIME_SPECIFICALLY_FORMATTED);
    }
}