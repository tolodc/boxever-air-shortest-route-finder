package com.boxever.air.shortestroute.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class Route {

    @NonNull
    private final String departureAirport;

    @NonNull
    private final String arrivalAirport;

    @NonNull
    private final int hours;

}
