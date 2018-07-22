package com.ryanair.task.interconnectingflights.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
public class RestClientCommon {
    private static final String logMsg = "Sending HTTP request to url: %s";

    /**
     * Common method for HTTP GET requests to another services which could return list of objects.
     *
     * @param parameterizedTypeReference - specific ParameterizedTypeReference object.
     * @param serviceUrl                 - external service specific url.
     * @return list of <T> objects.
     */
    protected <T> List<T> sendGetRequestForListOfObjects(
            final ParameterizedTypeReference<List<T>> parameterizedTypeReference, String serviceUrl) {

        log.info(String.format(logMsg, serviceUrl));

        return new RestTemplate().exchange(
                serviceUrl,
                HttpMethod.GET,
                null,
                parameterizedTypeReference)
                .getBody();
    }

    /**
     * Common method for HTTP GET requests to another services which could return one object.
     *
     * @param parameterizedTypeReference - specific ParameterizedTypeReference object.
     * @param serviceUrl                 - external service specific url.
     * @return <T> object.
     */
    protected <T> T sendGetRequestForOneObject(final ParameterizedTypeReference<T> parameterizedTypeReference,
                                               String serviceUrl) {

        log.info(String.format(logMsg, serviceUrl));

        return new RestTemplate().exchange(
                serviceUrl,
                HttpMethod.GET,
                null,
                parameterizedTypeReference)
                .getBody();
    }
}

