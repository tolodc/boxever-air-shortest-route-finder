package com.boxever.air.shortestroute.application.dijsktra;

import com.boxever.air.shortestroute.application.dijsktra.model.RouteNode;
import com.boxever.air.shortestroute.application.port.ShortestDistancePrinter;
import org.springframework.stereotype.Component;

@Component
public class ShortestDistanceDijkstraPrinter implements ShortestDistancePrinter<RouteNode> {

    @Override
    public String print(RouteNode result) {
        return "";
    }
}
