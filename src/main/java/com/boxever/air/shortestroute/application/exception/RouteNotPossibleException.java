package com.boxever.air.shortestroute.application.exception;

public class RouteNotPossibleException extends RuntimeException {
    public RouteNotPossibleException(final String message) {
        super(message);
    }
}
