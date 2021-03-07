package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.adapter.repository.RouteRepository;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import com.boxever.air.shortestroute.domain.model.Route;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortestRouteDijkstraBuilderTest {

    private static final String AIRPORT_ONE = "AIRPORT_ONE";
    private static final String AIRPORT_TWO = "AIRPORT_TWO";

    @Mock
    private RouteRepository repository;

    @InjectMocks
    private ShortestRouteDijkstraBuilder dijkstraBuilder;

    @Test
    void buildRouteNodes_should_call_repository_and_create_a_routemap_without_nodes_when_no_airports_given() {
        // Given
        final List<String> givenAirports = emptyList();
        when(repository.getAllAirports()).thenReturn(givenAirports);

        // When
        RouteMap routeMap = dijkstraBuilder.buildRouteMap();

        // Then
        assertThat(routeMap, is(notNullValue()));
        assertThat(routeMap.getRoutes(), anEmptyMap());
        verify(repository).getAllAirports();
    }

    @Test
    void buildRouteNodes_should_call_repository_and_create_a_routemap_with_a_route_node_for_each_given_airport() {
        // Given
        final String givenDepartureAirport = AIRPORT_ONE;
        final String givenArrivalAirport = AIRPORT_TWO;
        final List<String> givenAirports = List.of(givenDepartureAirport, givenArrivalAirport);
        when(repository.getAllAirports()).thenReturn(givenAirports);

        // When
        RouteMap routeMap = dijkstraBuilder.buildRouteMap();

        // Then
        assertRouteMapKeys(routeMap, givenDepartureAirport, givenArrivalAirport);

        verify(repository).getAllAirports();
    }

    @Test
    void buildRouteNodes_should_create_route_node_with_available_route_and_a_route_node_without_available_routes_given_only_a_route() {
        // Given
        final String givenDepartureAirport = AIRPORT_ONE;
        final String givenArrivalAirport = AIRPORT_TWO;
        final int givenDuration = 1;
        final List<String> givenAirports = List.of(givenDepartureAirport, givenArrivalAirport);
        final Route givenRoute = buildRoute(givenDepartureAirport, givenArrivalAirport, givenDuration);
        final List<Route> givenRoutes = List.of(givenRoute);

        when(repository.getAllAirports()).thenReturn(givenAirports);
        when(repository.getAllRoutes()).thenReturn(givenRoutes);

        // When
        RouteMap routeMap = dijkstraBuilder.buildRouteMap();

        // Then
        assertRouteMapKeys(routeMap, givenDepartureAirport, givenArrivalAirport);

        RouteNode departureRouteNode = routeMap.getRoutes().get(givenDepartureAirport);
        RouteNode arrivalRouteNode = routeMap.getRoutes().get(givenArrivalAirport);

        assertThat(departureRouteNode.getAvailableRoutes(), aMapWithSize(givenRoutes.size()));
        assertThat(departureRouteNode.getAvailableRoutes(), hasEntry(arrivalRouteNode, givenDuration));
        assertThat(arrivalRouteNode.getAvailableRoutes(), anEmptyMap());

        verify(repository).getAllAirports();
        verify(repository).getAllRoutes();

    }

    @Test
    void buildRouteNodes_should_create_route_node_with_available_routes_and_two_route_nodes_without_available_routes_given_only_two_route_from_same_departure() {
        // Given
        final String givenDepartureAirport = AIRPORT_ONE;
        final String givenArrivalAirport = AIRPORT_TWO;
        final String givenSecondArrivalAirport = "AIRPORT_THREE";
        final int givenDuration = 1;
        final int givenSecondDuration = 2;
        final List<String> givenAirports = List.of(givenDepartureAirport, givenArrivalAirport, givenSecondArrivalAirport);
        final Route givenFirstRoute = buildRoute(givenDepartureAirport, givenArrivalAirport, givenDuration);
        final Route givenSecondRoute = buildRoute(givenDepartureAirport, givenSecondArrivalAirport, givenSecondDuration);
        final List<Route> givenRoutes = List.of(givenFirstRoute, givenSecondRoute);

        when(repository.getAllAirports()).thenReturn(givenAirports);
        when(repository.getAllRoutes()).thenReturn(givenRoutes);

        // When
        RouteMap routeMap = dijkstraBuilder.buildRouteMap();

        // Then
        assertRouteMapKeys(routeMap, givenDepartureAirport, givenArrivalAirport, givenSecondArrivalAirport);

        RouteNode departureRouteNode = routeMap.getRoutes().get(givenDepartureAirport);
        RouteNode arrivalRouteNode = routeMap.getRoutes().get(givenArrivalAirport);
        RouteNode arrivalSecondRouteNode = routeMap.getRoutes().get(givenSecondArrivalAirport);

        assertThat(departureRouteNode.getAvailableRoutes(), aMapWithSize(givenRoutes.size()));
        assertThat(departureRouteNode.getAvailableRoutes(), hasEntry(arrivalRouteNode, givenDuration));
        assertThat(departureRouteNode.getAvailableRoutes(), hasEntry(arrivalSecondRouteNode, givenSecondDuration));
        assertThat(arrivalRouteNode.getAvailableRoutes(), anEmptyMap());

        verify(repository).getAllAirports();
        verify(repository).getAllRoutes();

    }

    @Test
    void buildRouteNodes_should_create_route_two_nodes_with_available_routes_and_a_route_node_without_available_routes_given_only_two_route_from_different_departure_and_same_arrival() {
        // Given
        final String givenDepartureAirport = AIRPORT_ONE;
        final String givenSecondDepartureAirport = AIRPORT_TWO;
        final String givenArrivalAirport = "AIRPORT_THREE";
        final int givenDuration = 1;
        final int givenSecondDuration = 2;
        final List<String> givenAirports = List.of(givenDepartureAirport, givenSecondDepartureAirport, givenArrivalAirport);
        final Route givenFirstRoute = buildRoute(givenDepartureAirport, givenArrivalAirport, givenDuration);
        final Route givenSecondRoute = buildRoute(givenSecondDepartureAirport, givenArrivalAirport, givenSecondDuration);
        final List<Route> givenRoutes = List.of(givenFirstRoute, givenSecondRoute);

        when(repository.getAllAirports()).thenReturn(givenAirports);
        when(repository.getAllRoutes()).thenReturn(givenRoutes);

        // When
        RouteMap routeMap = dijkstraBuilder.buildRouteMap();

        // Then
        assertRouteMapKeys(routeMap, givenDepartureAirport, givenSecondDepartureAirport, givenArrivalAirport);

        RouteNode departureRouteNode = routeMap.getRoutes().get(givenDepartureAirport);
        RouteNode departureSecondRouteNode = routeMap.getRoutes().get(givenSecondDepartureAirport);
        RouteNode arrivalRouteNode = routeMap.getRoutes().get(givenArrivalAirport);

        assertThat(departureRouteNode.getAvailableRoutes(), aMapWithSize(1));
        assertThat(departureRouteNode.getAvailableRoutes(), hasEntry(arrivalRouteNode, givenDuration));
        assertThat(departureSecondRouteNode.getAvailableRoutes(), aMapWithSize(1));
        assertThat(departureSecondRouteNode.getAvailableRoutes(), hasEntry(arrivalRouteNode, givenSecondDuration));
        assertThat(arrivalRouteNode.getAvailableRoutes(), anEmptyMap());

        verify(repository).getAllAirports();
        verify(repository).getAllRoutes();

    }

    private void assertRouteMapKeys(final RouteMap routeMap, final String... keys){
        assertThat(routeMap, is(notNullValue()));
        assertThat(routeMap.getRoutes(), aMapWithSize(keys.length));
        for (String key: keys) {
            assertThat(routeMap.getRoutes(), hasKey(key));
        }
    }

    private Route buildRoute(String givenDepartureAirport, String givenSecondArrivalAirport, int givenSecondDuration) {
        return Route.builder()
                .departureAirport(givenDepartureAirport)
                .arrivalAirport(givenSecondArrivalAirport)
                .hours(givenSecondDuration)
                .build();
    }
}
