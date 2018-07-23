package com.ryanair.task.interconnectingflights.utils;

import org.springframework.util.StringUtils;

/**
 * Provides method for validations
 */
public class Validator {

    /**
     * Returns provided String value if it is not equals to null or empty string
     * otherwise throws IllegalArgumentException
     *
     * @param value - input value which need to be checked.
     * @return String.
     */
    public String checkIfNullOrEmpty(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
