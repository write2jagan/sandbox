package it.balyfix.dsl.semantic;

import it.balyfix.dsl.semantic.model.Route;


public class RouteBuilder
{

    private Route route;

    public RouteBuilder()
    {
        route = new Route();
    }

    public static RouteBuilder route()
    {
        return new RouteBuilder();
    }

    public EndPointBuilder endPoint()
    {
        EndPointBuilder builder = new EndPointBuilder(this);

        getGraph().addEndPoint(builder.endPoint);

        return builder;
    }

    public Route getGraph()
    {
        return route;
    }

    public void printGraph()
    {
        Route.printRoute(route);
    }
}