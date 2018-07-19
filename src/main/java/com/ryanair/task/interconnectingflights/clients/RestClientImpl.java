package com.ryanair.task.interconnectingflights.clients;

import com.ryanair.task.interconnectingflights.constants.RestClientConstants;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableSchedulesDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestClientImpl extends RestClientCommon implements IRestClient {

    /**
     * Method send GET request to Schedule service.
     *
     * @return list of AvailableRoutsDto.
     */
    @Override
    public List<AvailableRoutsDto> getAllAvailableRouts() {
        return sendGetRequestForListOfObjects(
                new ParameterizedTypeReference<List<AvailableRoutsDto>>() {
                },
                RestClientConstants.ROUTES_API_LINK
        );
    }

    /**
     * Method sends GET request to Schedule service.
     *
     * @param scheduleServiceUrl - service url.
     * @return AvailableSchedulesDto object.
     */
    @Override
    public AvailableSchedulesDto getAllSchedulesByFilter(String scheduleServiceUrl) {
        return sendGetRequestForOneObject(
                new ParameterizedTypeReference<AvailableSchedulesDto>() {
                },
                scheduleServiceUrl);
    }
}
