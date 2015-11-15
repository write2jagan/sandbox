/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package it.balyfix.rs.main;

import it.balyfix.rs.model.Credentials;
import it.balyfix.rs.model.Customer;
import it.balyfix.rs.model.Order;
import it.balyfix.rs.model.Shipment;
import it.balyfix.utils.Futures;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ClientRS
{

    private static final ExecutorService service = Executors.newFixedThreadPool(4);

    private static Logger log = LoggerFactory.getLogger(ClientRS.class);

    public static void main(String args[]) throws Exception
    {

        CompletableFuture<Credentials> orderLogin = loginTo("order");

        CompletableFuture<Credentials> customerLogin = loginTo("customer");

        CompletableFuture<Order> order = orderLogin.thenCompose(item -> retriveOrder(item.getOrderId()));

        Shipment shipment = customerLogin.thenCompose(login -> retriveCustomer(login.getOrderId())).thenCombine(
            order,
            (customer, orderItem) -> new Shipment(customer, orderItem)).join();
        
        log.info(shipment.getCustomer().getEmail());
        log.info(shipment.getOrders().get(0).getAmount().toString());
    
    
    }

    private static CompletableFuture<Customer> retriveCustomer(Long id)
    {
        Client client = ClientBuilder.newBuilder().newClient();
        return Futures.toCompletable(client
            .target("http://localhost:9000/customerservice/customers/{id}")
            .resolveTemplate("id", id)
            .request()
            .async()
            .get(Customer.class), service);

    }

    // return loginTo("track")
    // .thenCompose(trackLogin -> lookupTracks(albumName, trackLogin)) // <2>
    // .thenCombine(artistLookup, (tracks, artists)
    // -> new Album(albumName, tracks, artists)) // <3>
    // .join(); // <4>
    //

    private static CompletableFuture<Order> retriveOrder(Long id)
    {
        Client client = ClientBuilder.newBuilder().newClient();
        return Futures.toCompletable(
            client
                .target("http://localhost:9000/orderservice/orders/{id}")
                .resolveTemplate("id", String.valueOf(id))
                .request()
                .async()
                .get(Order.class),
            service);
    }

    private static CompletableFuture<Credentials> loginTo(String serviceName)
    {
        return CompletableFuture.supplyAsync(() -> {
            if ("order".equals(serviceName))
            {
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return new Credentials();
        }, service);
    }

}
