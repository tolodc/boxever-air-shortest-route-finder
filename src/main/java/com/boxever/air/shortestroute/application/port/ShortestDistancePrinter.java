package com.boxever.air.shortestroute.application.port;

public interface ShortestDistancePrinter<T> {
    String print(final T result);
}
