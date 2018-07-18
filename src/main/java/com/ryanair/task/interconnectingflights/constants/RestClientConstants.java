package com.ryanair.task.interconnectingflights.constants;

/**
 * RestClientConstants class contains all useful constant variables for rest clients.
 */
public final class RestClientConstants {

    public static final String ROUTES_API_LINK = "https://api.ryanair.com/core/3/routes/";
    public static final String SCHEDULES_API_LINK = "https://api.ryanair.com/timetable/3/schedules/" +
            "{departure}/{arrival}/years/{year}/months/{month}";

    /**
     * Default private constructor to avoid class instantiation.
     */
    private RestClientConstants() {
    }
}
