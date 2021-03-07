package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortestRouteDijkstraFinderTest {

    private static final String DUB = "DUB";
    private static final String LHR = "LHR";
    private static final String BKK = "BKK";
    private static final String SYD = "SYD";

    @Mock
    private ShortestRouteDijkstraBuilder builder;

    @InjectMocks
    private ShortestRouteDijkstraFinder dijkstraFinder;

    @Test
    void find_should_call_repository_and_throw_exception_if_given_departure_not_found() {
        // Given
        final String givenDeparture = DUB;
        final String givenArrival = SYD;
        final RouteMap givenRouteMap = buildRouteMap(RouteNode.builder().airport(givenArrival).build());

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        assertThrows(IllegalArgumentException.class, () -> dijkstraFinder.find(givenDeparture, givenArrival));

        // Then
        verify(builder).buildRouteMap();
    }

    @Test
    void find_should_call_repository_and_throw_exception_if_given_arrival_not_found() {
        // Given
        final String givenDeparture = DUB;
        final String givenArrival = SYD;
        final RouteMap givenRouteMap = buildRouteMap(RouteNode.builder().airport(givenDeparture).build());

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        assertThrows(IllegalArgumentException.class, () -> dijkstraFinder.find(givenDeparture, givenArrival));

        // Then
        verify(builder).buildRouteMap();
    }

    @Test
    void find_should_find_destination_if_route_is_possible() {
        // Given
        final RouteNode sydRouteNode = RouteNode.builder().airport(SYD).build();
        final RouteNode bkkRouteNode = RouteNode.builder().airport(BKK).availableRoutes(Map.of(sydRouteNode, 11)).build();
        final RouteNode lhrRouteNode = RouteNode.builder().airport(LHR).availableRoutes(Map.of(bkkRouteNode, 9)).build();
        final RouteNode dubRouteNode = RouteNode.builder().airport(DUB).availableRoutes(Map.of(lhrRouteNode, 1)).build();
        final int expectedTotalDistance = 11+9+1;

        final RouteMap givenRouteMap = buildRouteMap(sydRouteNode, bkkRouteNode, lhrRouteNode, dubRouteNode);

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        dijkstraFinder.find(DUB, SYD);

        // Then
        assertThat(sydRouteNode.getTotalDistance(), is(expectedTotalDistance));
        verify(builder).buildRouteMap();

    }

    private RouteMap buildRouteMap(final RouteNode... routeNodes) {
        Map<String, RouteNode> routeNodesMap = new HashMap<>();

        for (RouteNode routeNode: routeNodes) {
            routeNodesMap.put(routeNode.getAirport(), routeNode);
        }

        return RouteMap.builder()
                .routes(routeNodesMap)
                .build();
    }
}
