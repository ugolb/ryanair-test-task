package com.ryanair.task.interconnectingflights.utils;

import com.ryanair.task.interconnectingflights.constants.RestClientConstants;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

public final class ApplicationUtils {

    /**
     * Private constructor to avoid util class instantiation
     */
    private ApplicationUtils() {
    }

    public static String buildScheduleServiceUrl(String departure, String arrival, final FlightFilterModel filter) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("departure", departure);
        parameters.put("arrival", arrival);
        parameters.put("year", DateTimeUtil.getYearInString(filter.getDepartureDateTime()));
        parameters.put("month", DateTimeUtil.getMonthInString(filter.getDepartureDateTime()));

        return UriComponentsBuilder.fromHttpUrl(RestClientConstants.SCHEDULES_API_LINK).build(parameters).toString();
    }
}
