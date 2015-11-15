package it.balyfix.stock.service;

import it.balyfix.stock.model.Stock;
import it.balyfix.stock.model.StockDetail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public interface StockService
{

    public static List<Stock> getData()
    {
        List<Stock> answer = new ArrayList<>();

        Stock stock = new Stock("stockA");
        answer.add(stock);
        stock.setStockCode("D");
        StockDetail stockDetail = new StockDetail();
        stockDetail.setCompName("sunshine");
        LocalDate now = LocalDate.now();
        stockDetail.setDate(now);
        stock.setStockDetail(stockDetail);
        stock.setStockId(1);

        Stock stock1 = new Stock("stockA");
        answer.add(stock1);
        stock1.setStockCode("C");
        StockDetail stockDetail1 = new StockDetail();
        stockDetail1.setCompName("sunshine");
        stockDetail1.setDate(now);
        stock1.setStockDetail(stockDetail1);
        stock1.setStockId(1);

        Stock stock2 = new Stock("stockb");
        answer.add(stock2);
        stock2.setStockCode("B");
        StockDetail stockDetail2 = new StockDetail();
        stockDetail2.setCompName("sunshine");
        stockDetail2.setDate(now);
        stock2.setStockDetail(stockDetail2);
        stock2.setStockId(1);

        Stock stock3 = new Stock("stockc");
        answer.add(stock3);
        stock3.setStockCode("A");
        StockDetail stockDetail3 = new StockDetail();
        stockDetail3.setCompName("sunshine");
        stockDetail3.setDate(now);
        stock3.setStockDetail(stockDetail3);
        stock3.setStockId(1);
        

        Stock stock4 = new Stock("stock4");
        answer.add(stock4);
        StockDetail stockDetail4 = new StockDetail();
        stockDetail4.setCompName("sunshine");
        stockDetail4.setDate(now);
        stock4.setStockDetail(stockDetail4);
        stock4.setStockId(1999);

        return answer;
    }

}
