package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteMap;
import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import com.boxever.air.shortestroute.application.exception.AirportNotFoundException;
import com.boxever.air.shortestroute.application.exception.RouteNotPossibleException;
import com.boxever.air.shortestroute.application.port.ShortestRouteFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ShortestRouteDijkstraFinder implements ShortestRouteFinder {

    private final ShortestRouteDijkstraBuilder builder;
    private final ShortestDistanceDijkstraPrinter printer;

    @Override
    public String find(String departure, String arrival) {
        RouteMap routeMap = builder.buildRouteMap();

        if (!routeMap.getRoutes().containsKey(departure)) {
            throw new AirportNotFoundException("Departure airport not found.");
        }

        if (!routeMap.getRoutes().containsKey(arrival)) {
            throw new AirportNotFoundException("Arrival airport not found.");
        }

        RouteNode destinationRoute = buildShortestRoute(routeMap, departure, arrival);

        if (!destinationRoute.isDestinationReached()) {
            throw new RouteNotPossibleException("Destination not reachable with current air connectivity.");
        }

        return printer.print(destinationRoute);
    }

    public RouteNode buildShortestRoute(final RouteMap routeMap, final String departure, final String arrival) {

        RouteNode sourceRoute = routeMap.getRoutes().get(departure);
        RouteNode destinationRoute = routeMap.getRoutes().get(arrival);
        sourceRoute.setTotalDistance(0);

        routeMap.addUnsettledRoute(sourceRoute);

        while (!CollectionUtils.isEmpty(routeMap.getUnsettledRoutes())) {
            RouteNode route = findClosestNode(routeMap.getUnsettledRoutes());
            routeMap.removeUnsettledRoute(route);

            if (isFeasibleRoute(route, destinationRoute)) {
                route.getAvailableRoutes().forEach((connectingRoute, distance) -> {
                    if (isFeasibleConnectingRoute(route, connectingRoute, destinationRoute, distance, routeMap.getSettledRoutes())) {
                        buildConnectingRoute(route, connectingRoute, distance);
                        routeMap.addUnsettledRoute(connectingRoute);
                    }
                });
            }

            routeMap.addSettledRoute(route);
        }

        return destinationRoute;
    }

    private boolean isFeasibleRoute(final RouteNode route, final RouteNode destination) {
        return !isExceededDistance(route.getTotalDistance(), destination);
    }

    private boolean isFeasibleConnectingRoute(final RouteNode route, final RouteNode connectingRoute, final RouteNode destinationRoute,
                                              final Integer distance, final Set<RouteNode> settledNodes) {
        return  !settledNodes.contains(connectingRoute)
                && !isExceededDistance(route.getTotalDistance() + distance, destinationRoute)
                && !isExceededDistance(route.getTotalDistance() + distance, connectingRoute);
    }

    private boolean isExceededDistance(final int distance, final RouteNode arrivalNode) {
        return distance >= arrivalNode.getTotalDistance();
    }

    private RouteNode findClosestNode(Set<RouteNode> routes) {
        return routes.stream()
                .min(Comparator.comparing(RouteNode::getTotalDistance))
                .orElseThrow(NoSuchElementException::new);
    }

    private void buildConnectingRoute(RouteNode sourceRoute, RouteNode connectingRoute, Integer distance) {
        LinkedList<RouteNode> shortestPath = new LinkedList<>(sourceRoute.getShortestRoute());
        shortestPath.add(sourceRoute);
        connectingRoute.setShortestRoute(shortestPath);
        connectingRoute.setTotalDistance(sourceRoute.getTotalDistance() + distance);
    }
}
