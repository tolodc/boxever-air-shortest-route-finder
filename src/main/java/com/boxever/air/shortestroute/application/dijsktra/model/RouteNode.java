package com.boxever.air.shortestroute.application.dijsktra.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class RouteNode {

    private final String airport;

    @Builder.Default
    private Integer totalDistance = Integer.MAX_VALUE;

    @Builder.Default
    private List<RouteNode> shortestRoute = new LinkedList<>();

    @Builder.Default
    private Map<RouteNode, Integer> availableRoutes = new HashMap<>();

    public boolean isDestinationReached() {
        return totalDistance != Integer.MAX_VALUE;
    }
}