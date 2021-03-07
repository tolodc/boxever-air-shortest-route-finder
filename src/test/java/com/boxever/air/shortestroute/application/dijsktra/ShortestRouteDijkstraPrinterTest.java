package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class ShortestRouteDijkstraPrinterTest {

    private static final String DUB = "DUB";
    private static final String LHR = "LHR";
    private static final String BKK = "BKK";
    private static final String SYD = "SYD";

    @InjectMocks
    private ShortestDistanceDijkstraPrinter dijkstraPrinter;

    @Test
    void printer_should_print() {
        // Given
        final RouteNode dubRouteNode = RouteNode.builder().airport(DUB).totalDistance(0).build();
        final RouteNode lhrRouteNode = RouteNode.builder().airport(LHR).totalDistance(1).build();
        final RouteNode bkkRouteNode = RouteNode.builder().airport(BKK).totalDistance(10).build();

        final LinkedList<RouteNode> shortestRoute = new LinkedList<>();
        shortestRoute.add(dubRouteNode);
        shortestRoute.add(lhrRouteNode);
        shortestRoute.add(bkkRouteNode);

        final String expectedPrint = "DUB -- LHR (1)\n" + "LHR -- BKK (9)\n" + "BKK -- SYD (11)\n" + "Time: 21";

        final RouteNode givenRouteNode = RouteNode.builder().airport(SYD).shortestRoute(shortestRoute).totalDistance(21).build();

        // When
        String print = dijkstraPrinter.print(givenRouteNode);

        // Then
        assertThat(print, is(expectedPrint));
    }

}
