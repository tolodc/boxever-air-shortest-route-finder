package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
class ShortestRouteDijkstraPrinterTest {

    @InjectMocks
    private ShortestDistanceDijkstraPrinter dijkstraPrinter;

    @Test
    void printer_should_print() {
        // Given
        final RouteNode givenRouteNode = RouteNode.builder().airport("XXX").build();

        // When
        String print = dijkstraPrinter.print(givenRouteNode);

        // Then
        assertThat(print, is(notNullValue()));
    }

}
