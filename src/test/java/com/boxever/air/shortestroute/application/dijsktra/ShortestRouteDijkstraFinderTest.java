package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortestRouteDijkstraFinderTest {

    @Mock
    private ShortestRouteDijkstraBuilder builder;

    @InjectMocks
    private ShortestRouteDijkstraFinder dijkstraFinder;

    @Test
    void find_should_call_repository_and_throw_exception_if_given_departure_not_found() {
        // Given
        final String givenDeparture = "XXX";
        final String givenArrival = "YYY";

        final Map<String, RouteNode> routeNodes = Map.of(
                givenArrival, RouteNode.builder().airport(givenArrival).build()
        );
        final RouteMap givenRouteMap = RouteMap.builder()
                .routes(routeNodes)
                .build();

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        assertThrows(IllegalArgumentException.class, () -> dijkstraFinder.find(givenDeparture, givenArrival));

        // Then
        verify(builder).buildRouteMap();
    }

    @Test
    void find_should_call_repository_and_throw_exception_if_given_arrival_not_found() {
        // Given
        final String givenDeparture = "XXX";
        final String givenArrival = "YYY";

        final Map<String, RouteNode> routeNodes = Map.of(
                givenArrival, RouteNode.builder().airport(givenDeparture).build()
        );
        final RouteMap givenRouteMap = RouteMap.builder()
                .routes(routeNodes)
                .build();

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        assertThrows(IllegalArgumentException.class, () -> dijkstraFinder.find(givenDeparture, givenArrival));

        // Then
        verify(builder).buildRouteMap();
    }

}
