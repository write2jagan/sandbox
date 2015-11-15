package it.balyfix.dsl.semantic.model;

import java.util.ArrayList;
import java.util.List;


public class Route
{

    private List<Endpoint> endPoints;

    public Route()
    {
        endPoints = new ArrayList<>();
    }

    public void addEndPoint(Endpoint endPoint)
    {
        getEndpoint().add(endPoint);
    }

    public List<Endpoint> getEndpoint()
    {
        return endPoints;
    }

    public static void printRoute(Route g)
    {
        System.out.println("");
        System.out.println("Endpoint...");
        for (Endpoint e : g.getEndpoint())
        {
            System.out.println(e);
        }
    }
}