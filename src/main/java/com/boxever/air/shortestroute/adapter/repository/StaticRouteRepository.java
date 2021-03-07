package com.boxever.air.shortestroute.adapter.repository;

import com.boxever.air.shortestroute.domain.model.Route;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaticRouteRepository implements RouteRepository, InitializingBean {

    private List<Route> routes;

    @Override
    public List<Route> getAllRoutes() {
        return routes;
    }

    @Override
    public List<String> getAllAirports() {
        return Stream.concat(routes.stream().map(Route::getDepartureAirport), routes.stream().map(Route::getArrivalAirport))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() {
        routes = new ArrayList<>();
        routes.add(Route.builder().departureAirport("DUB").arrivalAirport("LHR").hours(1).build());
        routes.add(Route.builder().departureAirport("DUB").arrivalAirport("CDG").hours(2).build());
        routes.add(Route.builder().departureAirport("CDG").arrivalAirport("BOS").hours(6).build());
        routes.add(Route.builder().departureAirport("CDG").arrivalAirport("BKK").hours(9).build());
        routes.add(Route.builder().departureAirport("ORD").arrivalAirport("LAS").hours(2).build());
        routes.add(Route.builder().departureAirport("LHR").arrivalAirport("NYC").hours(5).build());
        routes.add(Route.builder().departureAirport("NYC").arrivalAirport("LAS").hours(3).build());
        routes.add(Route.builder().departureAirport("BOS").arrivalAirport("LAX").hours(4).build());
        routes.add(Route.builder().departureAirport("LHR").arrivalAirport("BKK").hours(9).build());
        routes.add(Route.builder().departureAirport("BKK").arrivalAirport("SYD").hours(11).build());
        routes.add(Route.builder().departureAirport("LAX").arrivalAirport("LAS").hours(2).build());
        routes.add(Route.builder().departureAirport("DUB").arrivalAirport("ORD").hours(6).build());
        routes.add(Route.builder().departureAirport("LAX").arrivalAirport("SYD").hours(13).build());
        routes.add(Route.builder().departureAirport("LAS").arrivalAirport("SYD").hours(14).build());
    }
}
