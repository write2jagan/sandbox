package it.balyfix.rxjava;

import it.balyfix.rs.model.Order;
import it.balyfix.rs.model.OrderType;
import it.balyfix.rs.services.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import rx.Observable;


public class RxExample
{
    private List<Order> savedOrder;

    private List<OrderType> ordersType;
    

    public static void main(String[] args)
    {
        RxExample rxExample = new  RxExample(OrderService.generateOrders());
        
        
        Order defaultValue = new Order();
        defaultValue.setName("Default");
        defaultValue.setOrderType(OrderType.WEB_SITE_ORDER);
        defaultValue.setAmount(BigDecimal.valueOf(42));
        
        Observable<Order> search = rxExample.search(OrderType.WEB_SITE_ORDER, 5);
        search.defaultIfEmpty(defaultValue);
        search.subscribe(order -> System.out.println("Order type " + order.getOrderType() + " value " + order.getAmount()));
        
        System.out.println("Finish");
        
        
    }


    public RxExample(List<Order> savedArtists)
    {
        this.savedOrder = savedArtists;
        this.ordersType = Arrays.asList(OrderType.values());
       
        
    }

    public Observable<Order> search(OrderType typeTosearch, int maxResults)
    {

        return getOrderType()
            .filter(type -> type == typeTosearch)
            .flatMap(this::lookupArtist)
            .take(maxResults);
    }

    private Observable<OrderType> getOrderType()
    {
        return Observable.from(ordersType);
    }

    private Observable<Order> lookupArtist(OrderType type)
    {
        // other possible webservice Call
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Order required = savedOrder.stream().filter(order -> order.getOrderType() == type ).findFirst().get();
        return Observable.from(Arrays.asList(required));
    }

}
