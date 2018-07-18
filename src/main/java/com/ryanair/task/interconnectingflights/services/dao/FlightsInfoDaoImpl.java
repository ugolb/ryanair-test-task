package com.ryanair.task.interconnectingflights.services.dao;

import com.ryanair.task.interconnectingflights.clients.RestClientImpl;
import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableRoutsDto;
import com.ryanair.task.interconnectingflights.services.dtos.AvailableSchedulesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class FlightsInfoDaoImpl implements FlightsInfoDao {

    private RestClientImpl restClient;

    @Autowired
    public FlightsInfoDaoImpl(RestClientImpl restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<AvailableRoutsDto> getAllAvailableRoutes() {
        return restClient.getAllAvailableRouts();
    }

    @Override
    public AvailableSchedulesDto getFlightsSchedule(@NotNull FlightFilterModel filter) {
        return null;
    }
}
