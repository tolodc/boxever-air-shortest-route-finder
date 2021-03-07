package com.boxever.air.shortestroute.application.dijsktra.model;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Builder
public class RouteMap {

    @Builder.Default
    private final Map<String, RouteNode> routes = new HashMap<>();

    @Builder.Default
    private final Set<RouteNode> settledRoutes = new HashSet<>();

    @Builder.Default
    private final Set<RouteNode> unsettledRoutes = new HashSet<>();

}