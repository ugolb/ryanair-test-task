package com.ryanair.task.interconnectingflights.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    /**
     * Private constructor to avoid util class instantiation
     */
    private DateTimeUtil() {
    }

    /**
     * Method converts input string date-time representation to LocalDateTime object.
     *
     * @param valueToConvert - String value which need to be converted.
     * @return LocalDateTime object.
     */
    public static LocalDateTime convertStringToLocalDateTime(String valueToConvert) {
        return LocalDateTime.parse(valueToConvert);
    }

    /**
     * Method converts input LocalDateTime object to String with ISO date time standard.
     * Format example: 2011-12-03T10:15:30
     *
     * @param valueToConvert - LocalDateTime value which need to be converted.
     * @return String object.
     */
    public static String convertLocalDateTimeToString(final LocalDateTime valueToConvert) {
        final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return valueToConvert.format(dateTimeFormat);
    }

    /**
     * Method converts input LocalDateTime object to String with format specified via method parameter @dateTimeFormat.
     * @param valueToConvert - LocalDateTime value which need to be converted.
     * @param dateTimeFormat - DateTimeFormatter which will be applied to String representation of date-time.
     * @return - String object.
     */
    public static String convertLocalDateTimeToString(final LocalDateTime valueToConvert,
                                                      final DateTimeFormatter dateTimeFormat) {
        return valueToConvert.format(dateTimeFormat);
    }
}
