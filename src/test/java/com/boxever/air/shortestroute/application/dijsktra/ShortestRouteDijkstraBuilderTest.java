package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.adapter.repository.RouteRepository;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortestRouteDijkstraBuilderTest {

    @Mock
    private RouteRepository repository;

    @InjectMocks
    private ShortestRouteDijkstraBuilder dijkstraBuilder;

    @Test
    void buildRouteNodes_should_call_repository_and_create_a_routemap_without_nodes_when_no_airports_given() {
        // Given
        final Set<String> givenAirports = emptySet();
        when(repository.findAllAirports()).thenReturn(givenAirports);

        // When
        RouteMap routeMap = dijkstraBuilder.buildRouteNodes();

        // Then
        assertThat(routeMap, is(notNullValue()));
        assertThat(routeMap.getRoutes(), anEmptyMap());
        verify(repository).findAllAirports();
    }

    @Test
    void buildRouteNodes_should_call_repository_and_create_a_routemap_with_a_route_node_for_each_given_airport() {
        // Given
        final Set<String> givenAirports = Set.of("AIRPORT_ONE", "AIRPORT_TWO");
        when(repository.findAllAirports()).thenReturn(givenAirports);

        // When
        RouteMap routeMap = dijkstraBuilder.buildRouteNodes();

        // Then
        assertThat(routeMap, is(notNullValue()));
        assertThat(routeMap.getRoutes(), aMapWithSize(givenAirports.size()));
        verify(repository).findAllAirports();
    }

}
