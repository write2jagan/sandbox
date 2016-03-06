package it.balyfix.rs.model;

import java.util.ArrayList;
import java.util.List;


public class Shipment
{

    private Customer customer;
    
    private List<Order> orders ;
    
    
    public Shipment(Customer customer,Order order)
    {
        super();
        this.customer = customer;
        this.orders = new ArrayList<Order>();
        orders.add(order);
    }
    
    public Customer getCustomer()
    {
        return customer;
    }
    
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }
    
    public List<Order> getOrders()
    {
        return orders;
    }
    
    public void setOrders(List<Order> orders)
    {
        this.orders = orders;
    }
}
