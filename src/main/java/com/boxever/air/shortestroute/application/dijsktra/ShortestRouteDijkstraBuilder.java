package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.adapter.repository.RouteRepository;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShortestRouteDijkstraBuilder {

    private final RouteRepository repository;

    public RouteMap buildRouteNodes() {

        Map<String, RouteNode> routeNodes = repository.findAllAirports().stream()
                .collect(Collectors.toMap(
                        airport -> airport,
                        airport -> RouteNode.builder().airport(airport).build()));

        return RouteMap.builder()
                .routes(routeNodes)
                .build();
    }
}
