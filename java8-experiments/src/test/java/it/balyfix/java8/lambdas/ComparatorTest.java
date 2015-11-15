package it.balyfix.java8.lambdas;

import it.balyfix.stock.model.Stock;
import it.balyfix.stock.service.StockService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import static java.util.stream.Collectors.toList;


public class ComparatorTest
{

    @Test
    public void oldCompartor()
    {
        List<Stock> data = StockService.getData();

        Assert.assertEquals("D", data.get(0).getStockCode());

        Collections.sort(data, new Comparator<Stock>()
        {

            @Override
            public int compare(Stock stock1, Stock stock2)
            {
                return stock1.getStockCode().get().compareTo(stock2.getStockCode().get());
            }
        });

        Assert.assertEquals("A", data.get(0).getStockCode());

    }

    @Test
    public void newCompartor()
    {
        List<Stock> data = StockService.getData();
        Assert.assertEquals("D", data.get(0).getStockCode());
        List<Stock> collect = data
            .stream()
            .sorted((p1, p2) -> p1.getStockCode().get().compareTo(p2.getStockCode().get()))
            .collect(toList());
        Assert.assertEquals("A", collect.get(0).getStockCode());

    }

}
