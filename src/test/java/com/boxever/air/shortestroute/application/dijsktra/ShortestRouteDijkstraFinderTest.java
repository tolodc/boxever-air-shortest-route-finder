package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import com.boxever.air.shortestroute.application.exception.AirportNotFoundException;
import com.boxever.air.shortestroute.application.exception.RouteNotPossibleException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortestRouteDijkstraFinderTest {

    private static final String DUB = "DUB";
    private static final String CDG = "CDG";
    private static final String LHR = "LHR";
    private static final String BKK = "BKK";
    private static final String SYD = "SYD";
    private static final String BOS = "BOS";

    @Mock
    private ShortestRouteDijkstraBuilder builder;

    @Mock
    private ShortestDistanceDijkstraPrinter printer;

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
        assertThrows(AirportNotFoundException.class, () -> dijkstraFinder.find(givenDeparture, givenArrival));

        // Then
        verify(builder).buildRouteMap();
        verify(printer, times(0)).print(any());
    }

    @Test
    void find_should_call_repository_and_throw_exception_if_given_arrival_not_found() {
        // Given
        final String givenDeparture = DUB;
        final String givenArrival = SYD;
        final RouteMap givenRouteMap = buildRouteMap(RouteNode.builder().airport(givenDeparture).build());
        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        assertThrows(AirportNotFoundException.class, () -> dijkstraFinder.find(givenDeparture, givenArrival));

        // Then
        verify(builder).buildRouteMap();
        verify(printer, times(0)).print(any());
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
        assertThat(sydRouteNode.getShortestRoute(), contains(dubRouteNode, lhrRouteNode, bkkRouteNode));
        assertThat(givenRouteMap.getUnsettledRoutes(), empty());
        assertThat(givenRouteMap.getSettledRoutes(), containsInAnyOrder(sydRouteNode, bkkRouteNode, lhrRouteNode, dubRouteNode));
        verify(builder).buildRouteMap();
        verify(printer).print(sydRouteNode);
    }

    @Test
    void find_should_throw_exception_if_destination_is_not_possible() {
        // Given
        final RouteNode sydRouteNode = RouteNode.builder().airport(SYD).build();
        final RouteNode bkkRouteNode = RouteNode.builder().airport(BKK).availableRoutes(Map.of(sydRouteNode, 11)).build();
        final RouteNode lhrRouteNode = RouteNode.builder().airport(LHR).availableRoutes(Map.of(bkkRouteNode, 9)).build();
        final RouteNode dubRouteNode = RouteNode.builder().airport(DUB).availableRoutes(Map.of(lhrRouteNode, 1)).build();

        final RouteMap givenRouteMap = buildRouteMap(sydRouteNode, bkkRouteNode, lhrRouteNode, dubRouteNode);
        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        assertThrows(RouteNotPossibleException.class, () -> dijkstraFinder.find(SYD, DUB));

        // Then
        verify(builder).buildRouteMap();
        verify(printer, times(0)).print(any());
    }

    @Test
    void find_should_find_shortest_route_if_two_routes_are_possible() {
        // Given
        final RouteNode sydRouteNode = RouteNode.builder().airport(SYD).build();
        final RouteNode bkkRouteNode = RouteNode.builder().airport(BKK).availableRoutes(Map.of(sydRouteNode, 11)).build();
        final RouteNode lhrRouteNode = RouteNode.builder().airport(LHR).availableRoutes(Map.of(bkkRouteNode, 9)).build();
        final RouteNode cdgRouteNode = RouteNode.builder().airport(CDG).availableRoutes(Map.of(bkkRouteNode, 9)).build();
        final RouteNode dubRouteNode = RouteNode.builder().airport(DUB).availableRoutes(Map.of(lhrRouteNode, 1, cdgRouteNode, 2)).build();
        final int expectedTotalDistance = 11+9+1;

        final RouteMap givenRouteMap = buildRouteMap(sydRouteNode, bkkRouteNode, lhrRouteNode, dubRouteNode);

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        dijkstraFinder.find(DUB, SYD);

        // Then
        assertThat(sydRouteNode.getTotalDistance(), is(expectedTotalDistance));
        assertThat(sydRouteNode.getShortestRoute(), contains(dubRouteNode, lhrRouteNode, bkkRouteNode));
        assertThat(givenRouteMap.getSettledRoutes(), containsInAnyOrder(sydRouteNode, bkkRouteNode, lhrRouteNode, cdgRouteNode, dubRouteNode));
        assertThat(givenRouteMap.getUnsettledRoutes(), empty());
        verify(builder).buildRouteMap();
        verify(printer).print(sydRouteNode);
    }

    @Test
    void find_should_discard_route_if_current_distance_is_already_larger_than_destination() {
        // Given
        final RouteNode sydRouteNode = RouteNode.builder().airport(SYD).build();
        final RouteNode bkkRouteNode = RouteNode.builder().airport(BKK).availableRoutes(Map.of(sydRouteNode, 11)).build();
        final RouteNode bosRouteNode = RouteNode.builder().airport(BOS).availableRoutes(Map.of(sydRouteNode, 14)).build();
        final RouteNode lhrRouteNode = RouteNode.builder().airport(LHR).availableRoutes(Map.of(bkkRouteNode, 9)).build();
        final RouteNode cdgRouteNode = RouteNode.builder().airport(CDG).availableRoutes(Map.of(bkkRouteNode, 9, bosRouteNode, 6)).build();
        final RouteNode dubRouteNode = RouteNode.builder().airport(DUB).availableRoutes(Map.of(lhrRouteNode, 1, cdgRouteNode, 72)).build();
        final int expectedTotalDistance = 11+9+1;

        final RouteMap givenRouteMap = buildRouteMap(sydRouteNode, bkkRouteNode, lhrRouteNode, dubRouteNode);

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        dijkstraFinder.find(DUB, SYD);

        // Then
        assertThat(sydRouteNode.getTotalDistance(), is(expectedTotalDistance));
        assertThat(sydRouteNode.getShortestRoute(), contains(dubRouteNode, lhrRouteNode, bkkRouteNode));
        assertThat(givenRouteMap.getSettledRoutes(), containsInAnyOrder(sydRouteNode, bkkRouteNode, lhrRouteNode, cdgRouteNode, dubRouteNode));
        assertThat(givenRouteMap.getUnsettledRoutes(), empty());
        verify(builder).buildRouteMap();
        verify(printer).print(sydRouteNode);
    }

    @Test
    void find_should_discard_route_if_current_distance_plus_distance_is_already_larger_than_destination() {
        // Given
        final RouteNode sydRouteNode = RouteNode.builder().airport(SYD).build();
        final RouteNode bkkRouteNode = RouteNode.builder().airport(BKK).availableRoutes(Map.of(sydRouteNode, 11)).build();
        final RouteNode bosRouteNode = RouteNode.builder().airport(BOS).availableRoutes(Map.of(sydRouteNode, 14)).build();
        final RouteNode lhrRouteNode = RouteNode.builder().airport(LHR).availableRoutes(Map.of(bkkRouteNode, 9)).build();
        final RouteNode cdgRouteNode = RouteNode.builder().airport(CDG).availableRoutes(Map.of(bkkRouteNode, 9, bosRouteNode, 6)).build();
        final RouteNode dubRouteNode = RouteNode.builder().airport(DUB).availableRoutes(Map.of(lhrRouteNode, 1, cdgRouteNode, 20)).build();
        final int expectedTotalDistance = 11+9+1;

        final RouteMap givenRouteMap = buildRouteMap(sydRouteNode, bkkRouteNode, lhrRouteNode, dubRouteNode);

        when(builder.buildRouteMap()).thenReturn(givenRouteMap);

        // When
        dijkstraFinder.find(DUB, SYD);

        // Then
        assertThat(sydRouteNode.getTotalDistance(), is(expectedTotalDistance));
        assertThat(sydRouteNode.getShortestRoute(), contains(dubRouteNode, lhrRouteNode, bkkRouteNode));
        assertThat(givenRouteMap.getSettledRoutes(), containsInAnyOrder(sydRouteNode, bkkRouteNode, lhrRouteNode, cdgRouteNode, dubRouteNode));
        assertThat(givenRouteMap.getUnsettledRoutes(), empty());
        verify(builder).buildRouteMap();
        verify(printer).print(sydRouteNode);
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
