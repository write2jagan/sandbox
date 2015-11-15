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
package it.balyfix.rs.services;

import it.balyfix.rs.model.Order;
import it.balyfix.rs.model.OrderType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Path("/orderservice/")
@Produces("text/xml")
public class OrderService
{

    long currentId = 123;

    Map<Long, Order> orders = new HashMap<Long, Order>();

    Logger log = LoggerFactory.getLogger(this.getClass());

    public OrderService()
    {
        init();
    }

    @GET
    @Path("/orders/{id}/")
    public Order getOrder(@PathParam("id") String id)
    {
        log.info("----invoking getOrder, order id is: " + id);
        long idNumber = Long.parseLong(id);
        Order c = orders.get(idNumber);
        return c;
    }

    final void init()
    {
        Order order = new Order();
        order.setName("order1");
        order.setId(123);
        order.setAmount(BigDecimal.valueOf(100));
        orders.put(order.getId(), order);
    }

    public static List<Order> generateOrders()
    {

        List<Order> answer = new ArrayList<Order>();

        Random rn = new Random();

        int i = rn.nextInt() % 50;

        for (int x = 0; x < i; x++)
        {
            Order order = new Order();
            answer.add(order);
            order.setName(UUID.randomUUID().toString());
            order.setAmount(BigDecimal.valueOf(x));
            order.setOrderType(OrderType.values()[rn.nextInt(OrderType.values().length)]);
        }

        return answer;

    }

}
