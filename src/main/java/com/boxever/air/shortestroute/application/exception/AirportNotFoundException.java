package com.boxever.air.shortestroute.application.exception;

public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(final String message) {
        super(message);
    }
}
