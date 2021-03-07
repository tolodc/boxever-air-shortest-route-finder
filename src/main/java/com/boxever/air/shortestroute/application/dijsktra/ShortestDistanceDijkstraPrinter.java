package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import com.boxever.air.shortestroute.application.port.ShortestDistancePrinter;
import org.springframework.stereotype.Component;

@Component
public class ShortestDistanceDijkstraPrinter implements ShortestDistancePrinter<RouteNode> {

    @Override
    public String print(RouteNode result) {
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= result.getShortestRoute().size(); i++) {
            RouteNode departureNode = result.getShortestRoute().get(i-1);
            RouteNode arrivalNode = i==result.getShortestRoute().size() ? result : result.getShortestRoute().get(i);
            builder.append(printSingleRoute(departureNode, arrivalNode));
        }
        builder.append(printTotalTime(result));
        return builder.toString();
    }

    private StringBuilder printSingleRoute(final RouteNode departureNode, final RouteNode arrivalNode) {
        StringBuilder builder = new StringBuilder();
        builder.append(departureNode.getAirport()).append(" -- ").append(arrivalNode.getAirport())
                .append(" (").append(arrivalNode.getTotalDistance()- departureNode.getTotalDistance()).append(")").append("\n");
        return builder;
    }

    private StringBuilder printTotalTime(RouteNode arrivalNode) {
        StringBuilder builder = new StringBuilder();
        builder.append("Time: ").append(arrivalNode.getTotalDistance());
        return builder;
    }
}
