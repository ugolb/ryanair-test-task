package com.ryanair.task.interconnectingflights.services;

import com.ryanair.task.interconnectingflights.TestDataGenerator;
import com.ryanair.task.interconnectingflights.models.FoundFlightsModel;
import com.ryanair.task.interconnectingflights.services.analyzers.FlightScheduleAnalyzer;
import com.ryanair.task.interconnectingflights.services.dao.FlightsInfoDaoImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InterconnectingFlightsServiceTest {

    private FlightsInfoDaoImpl daoMock = Mockito.mock(FlightsInfoDaoImpl.class);
    private FlightScheduleAnalyzer analyzerMock = Mockito.mock(FlightScheduleAnalyzer.class);

    private InterconnectingFlightsService service = new InterconnectingFlightsService(analyzerMock, daoMock);


    @Test
    public void test() {
        //Given
        Mockito.when(daoMock.getAllAvailableRoutes()).thenReturn(TestDataGenerator.getListOfAvailableRoutsDto());
        List<FoundFlightsModel> appropriateFlights = service.getAppropriateFlights(TestDataGenerator.getWroBubFlightFilter());

        //When


        //Then


    }

}