package com.ryanair.task.interconnectingflights.utils;

import org.springframework.util.StringUtils;


public class Validator {

    public String checkIfNullOrEmpty(String value) {
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
