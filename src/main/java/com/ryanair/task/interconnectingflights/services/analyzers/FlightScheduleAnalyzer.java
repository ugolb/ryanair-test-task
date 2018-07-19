package com.ryanair.task.interconnectingflights.services.analyzers;

import com.ryanair.task.interconnectingflights.models.FlightFilterModel;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.dao.FlightsInfoDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlightScheduleAnalyzer implements Analyzer{

    private FlightsInfoDaoImpl flightsInfoDao;

    @Autowired
    public FlightScheduleAnalyzer(FlightsInfoDaoImpl flightsInfoDao) {
        this.flightsInfoDao = flightsInfoDao;
    }

    @Override
    public List<FoundFlightsModel> analyze(final FlightFilterModel flightFilterModel) {
        return null;
    }
}
