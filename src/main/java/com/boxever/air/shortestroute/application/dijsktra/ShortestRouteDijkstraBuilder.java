package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.adapter.repository.RouteRepository;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import com.boxever.air.shortestroute.domain.model.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Component
@RequiredArgsConstructor
public class ShortestRouteDijkstraBuilder {

    private final RouteRepository repository;

    public RouteMap buildRouteMap() {

        Map<String, List<Route>> routeConnectivity =
                repository.getAllRoutes().stream().collect(groupingBy(Route::getDepartureAirport));

        Map<String, RouteNode> routeNodes = repository.getAllAirports().stream()
                .collect(Collectors.toMap(
                        airport -> airport,
                        airport -> RouteNode.builder().airport(airport).build()));

        routeConnectivity.forEach((airport, routes) -> {
            RouteNode departureNode = routeNodes.get(airport);
            for (Route route : routes) {
                departureNode.getAvailableRoutes().put(routeNodes.get(route.getArrivalAirport()), route.getHours());
            }
        });

        return RouteMap.builder()
                .routes(routeNodes)
                .build();
    }
}
