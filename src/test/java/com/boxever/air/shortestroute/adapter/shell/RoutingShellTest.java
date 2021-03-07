package com.boxever.air.shortestroute.adapter.shell;

import com.boxever.air.shortestroute.application.port.ShortestRouteFinder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoutingShellTest {

    @Mock
    private ShortestRouteFinder shortestRouteFinder;

    @InjectMocks
    private RoutingShell routingShell;

    @Test
    void shortestRoute_should_call_finder_and_return_response() {
        // Given
        final String givenDepartureAirport = "XXX";
        final String givenArrivalAirport = "YYY";
        final String expectedShortestRoutePrint = "SHORTEST ROUTE";

        Mockito.when(shortestRouteFinder.find(givenDepartureAirport, givenArrivalAirport)).thenReturn(expectedShortestRoutePrint);

        // When
        String output = routingShell.shortestRoute(givenDepartureAirport, givenArrivalAirport);

        // Then
        assertThat(output, is(expectedShortestRoutePrint));
        verify(shortestRouteFinder).find(givenDepartureAirport, givenArrivalAirport);
    }

}
