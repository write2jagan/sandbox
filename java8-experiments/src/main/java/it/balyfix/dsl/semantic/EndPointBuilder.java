package it.balyfix.dsl.semantic;

import it.balyfix.dsl.semantic.model.Endpoint;
import it.balyfix.dsl.semantic.model.Vertex;


public class EndPointBuilder
{

    Endpoint endPoint;

    RouteBuilder gBuilder;

    public EndPointBuilder(RouteBuilder gBuilder)
    {
        this.gBuilder = gBuilder;
        endPoint = new Endpoint();
    }

    public EndPointBuilder from(String lbl)
    {
        Vertex v = new Vertex(lbl);
        endPoint.setFromVertex(v);
        return this;
    }

    public EndPointBuilder to(String lbl)
    {
        Vertex v = new Vertex(lbl);
        endPoint.setToVertex(v);
        return this;
    }

    public RouteBuilder end()
    {
        return gBuilder;
    }

}