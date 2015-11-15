package it.balyfix.dsl.semantic.model;

public class Endpoint
{

    private Vertex fromVertex;

    private Vertex toVertex;

    public Endpoint()
    {
    }

    public Endpoint(Vertex fromVertex, Vertex toVertex, Double weight)
    {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
    }

    @Override
    public String toString()
    {
        return fromVertex.getLabel() + " to " + toVertex.getLabel();
    }

    public Vertex getFromVertex()
    {
        return fromVertex;
    }

    public void setFromVertex(Vertex fromVertex)
    {
        this.fromVertex = fromVertex;
    }

    public Vertex getToVertex()
    {
        return toVertex;
    }

    public void setToVertex(Vertex toVertex)
    {
        this.toVertex = toVertex;
    }

}