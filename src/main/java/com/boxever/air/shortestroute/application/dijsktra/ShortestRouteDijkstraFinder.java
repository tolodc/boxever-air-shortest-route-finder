package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.port.ShortestRouteFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShortestRouteDijkstraFinder implements ShortestRouteFinder {

    private final ShortestRouteDijkstraBuilder builder;

    @Override
    public String find(String departure, String arrival) {
        RouteMap routeMap = builder.buildRouteNodes();

        if (!routeMap.getRoutes().containsKey(departure)) {
            throw new IllegalArgumentException("Departure airport not found.");
        }

        return null;
    }
}
