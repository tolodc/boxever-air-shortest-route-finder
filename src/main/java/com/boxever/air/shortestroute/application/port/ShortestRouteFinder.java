package com.boxever.air.shortestroute.application.port;

public interface ShortestRouteFinder {
    String find(final String departure, final String arrival);
}
