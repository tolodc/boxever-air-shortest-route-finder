package com.boxever.air.shortestroute.adapter.shell;

import com.boxever.air.shortestroute.application.port.ShortestRouteFinder;
import lombok.AllArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@AllArgsConstructor
public class RoutingShell {

    private final ShortestRouteFinder shortestRouteFinder;

    @ShellMethod(key = "route", value = "Find the shortest route between two airports")
    public String shortestRoute(final String departureAirport, final String arrivalAirport) {
        return shortestRouteFinder.find(departureAirport, arrivalAirport);
    }

}
