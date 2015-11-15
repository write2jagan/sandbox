package it.balyfix.java8.concurrent;

import static java.util.stream.Collectors.toList;
import it.balyfix.stock.service.StockService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;


public class SimpleCompletableFeature
{

    @Test
    public void asyncCall() throws InterruptedException, ExecutionException
    {

        List<CompletableFuture<String>> priceFutures = StockService
            .getData()
            .stream()
            .map(
                stock -> CompletableFuture.supplyAsync(() -> String.format(
                    "%s price is %.2f",
                    stock.getStockName(),
                    stock.getValue())))
            .collect(toList());
        String value = priceFutures.get(0).get();
        Assert.assertEquals("stockA price is 0.53", value);

    }

}
