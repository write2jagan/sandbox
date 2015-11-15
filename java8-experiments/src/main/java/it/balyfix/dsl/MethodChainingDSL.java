package it.balyfix.dsl;

import static it.balyfix.dsl.semantic.RouteBuilder.route;


public class MethodChainingDSL
{

    public static void main(String[] args)
    {
        route()
            .endPoint()
            .from("endPoint1")
            .to("endPoint1")
            .end()
            .endPoint()
            .from("endPoint3")
            .to("endPoint4")
            .end()
            .printGraph();
    }

}
