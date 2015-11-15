package it.balyfix.dsl.semantic.lambda;

import it.balyfix.dsl.semantic.model.Endpoint;
import it.balyfix.dsl.semantic.model.Vertex;


public class EndPointBuilder
{

    private Endpoint endpoint;

    public EndPointBuilder()
    {
        endpoint = new Endpoint();
    }

    public Endpoint endPoint()
    {
        return endpoint;
    }

    public void from(String lbl)
    {
        endpoint.setFromVertex(new Vertex(lbl));
    }

    public void to(String lbl)
    {
        endpoint.setToVertex(new Vertex(lbl));
    }

}