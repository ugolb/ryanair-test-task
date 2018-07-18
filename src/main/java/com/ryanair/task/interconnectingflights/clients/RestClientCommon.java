package com.ryanair.task.interconnectingflights.clients;

import com.ryanair.task.interconnectingflights.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.dtos.AvailableSchedulesDto;
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
     * Common method for inter-service call.
     *
     * @param parameterizedTypeReference - specific ParameterizedTypeReference object.
     * @param serviceUrl                 - external service specific url.
     * @return
     */
/*    protected <T> List<T> getDataFromOtherService(final ParameterizedTypeReference<List<T>> parameterizedTypeReference,
                                                  String serviceUrl) {

        return getHttpRequestFactory()
                .exchange(
                        serviceUrl,
                        HttpMethod.GET,
                        null,
                        parameterizedTypeReference)
                .getBody();
    }*/
    protected List<AvailableRoutsDto> getAvailableRoutsDto(
            final ParameterizedTypeReference<List<AvailableRoutsDto>> parameterizedTypeReference, String serviceUrl) {

        return getRestTemplate()
                .exchange(
                        serviceUrl,
                        HttpMethod.GET,
                        null,
                        parameterizedTypeReference)
                .getBody();
    }

    protected AvailableSchedulesDto getDataFromOtherServic(
            final ParameterizedTypeReference<AvailableSchedulesDto> parameterizedTypeReference, String serviceUrl) {

        return getRestTemplate()
                .exchange(
                        serviceUrl,
                        HttpMethod.GET,
                        null,
                        parameterizedTypeReference)
                .getBody();
    }

    /**
     * Prepares request factory which ignores SSL certificates.
     */
    private RestTemplate getRestTemplate() {
        final CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
        final HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }
}

