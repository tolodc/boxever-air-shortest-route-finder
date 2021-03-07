package com.boxever.air.shortestroute.adapter.repository;

import com.boxever.air.shortestroute.domain.model.Route;

import java.util.List;

public interface RouteRepository {
    List<Route> getAllRoutes();
    List<String> getAllAirports();
}
