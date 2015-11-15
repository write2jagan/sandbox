package it.balyfix.dsl.semantic.lambda;

import java.util.function.Consumer;

import it.balyfix.dsl.semantic.model.Endpoint;
import it.balyfix.dsl.semantic.model.Route;


public class RouteBuilder
{

    private Route route;

    public RouteBuilder()
    {
        route = new Route();
    }

    public static Route route(Consumer<RouteBuilder> routeBuilderConsumer)
    {
        RouteBuilder routeBuilder = new RouteBuilder();
        routeBuilderConsumer.accept(routeBuilder);
        return routeBuilder.route;
    }

    public EndPointBuilder endPoint(Consumer<EndPointBuilder> edgeBuilder)
    {
        EndPointBuilder builder = new EndPointBuilder();
        edgeBuilder.accept(builder);
        Endpoint e = builder.endPoint();
        route.addEndPoint(e);
        // getGraph().addEndPoint(builder.endPoint());

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