package com.boxever.air.shortestroute.adapter.repository;

import com.boxever.air.shortestroute.domain.model.Route;

import java.util.List;
import java.util.Set;

public interface RouteRepository {
    List<Route> findAllRoutes();
    Set<String> findAllAirports();
}
