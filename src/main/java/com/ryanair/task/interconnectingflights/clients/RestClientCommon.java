package com.ryanair.task.interconnectingflights.clients;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestClientCommon {

    /**
     * Common method for HTTP GET requests to another services which could return list of objects.
     *
     * @param parameterizedTypeReference - specific ParameterizedTypeReference object.
     * @param serviceUrl                 - external service specific url.
     * @return list of <T> objects.
     */
    protected <T> List<T> sendGetRequestForListOfObjects(
            final ParameterizedTypeReference<List<T>> parameterizedTypeReference, String serviceUrl) {

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
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

        return new RestTemplate().exchange(
                serviceUrl,
                HttpMethod.GET,
                null,
                parameterizedTypeReference)
                .getBody();
    }
}

