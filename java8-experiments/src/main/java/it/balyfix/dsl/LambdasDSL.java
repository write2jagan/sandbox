package it.balyfix.dsl;

import static it.balyfix.dsl.semantic.lambda.RouteBuilder.route;
import it.balyfix.dsl.semantic.model.Route;


public class LambdasDSL
{

    public static void main(String[] args)
    {

        Route routeSrc = route(r -> {
            r.endPoint(e -> {
                e.from("endPoint1");
                e.to("endPoint2");
            });
        });

        Route.printRoute(routeSrc);

    }
}
