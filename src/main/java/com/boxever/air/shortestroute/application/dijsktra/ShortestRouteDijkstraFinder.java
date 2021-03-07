package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import com.boxever.air.shortestroute.application.port.ShortestRouteFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ShortestRouteDijkstraFinder implements ShortestRouteFinder {

    private final ShortestRouteDijkstraBuilder builder;

    @Override
    public String find(String departure, String arrival) {
        RouteMap routeMap = builder.buildRouteMap();

        if (!routeMap.getRoutes().containsKey(departure)) {
            throw new IllegalArgumentException("Departure airport not found.");
        }

        if (!routeMap.getRoutes().containsKey(arrival)) {
            throw new IllegalArgumentException("Arrival airport not found.");
        }

        buildShortestRoute(routeMap, departure);

        return null;
    }

    public void buildShortestRoute(final RouteMap routeMap, final String departure) {

        RouteNode sourceRoute = routeMap.getRoutes().get(departure);
        sourceRoute.setTotalDistance(0);

        routeMap.addUnsettledRoute(sourceRoute);

        while (!CollectionUtils.isEmpty(routeMap.getUnsettledRoutes())) {
            RouteNode route = findClosestNode(routeMap.getUnsettledRoutes());
            routeMap.removeUnsettledRoute(route);

            route.getAvailableRoutes().forEach((connectingRoute, distance) -> {
                connectingRoute.setTotalDistance(route.getTotalDistance() + distance);
                routeMap.addUnsettledRoute(connectingRoute);
            });

            routeMap.addSettledRoute(route);
        }
    }

    private RouteNode findClosestNode(Set<RouteNode> routes) {
        return routes.stream()
                .min(Comparator.comparing(RouteNode::getTotalDistance))
                .orElseThrow(NoSuchElementException::new);
    }
}
