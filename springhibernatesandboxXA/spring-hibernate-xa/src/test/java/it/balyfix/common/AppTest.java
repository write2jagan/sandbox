package it.balyfix.common;

import it.balyfix.MainClass;
import it.balyfix.stock.bo.StockBo;
import it.balyfix.stock.model.Stock;
import it.balyfix.stock.model.StockDetail;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class AppTest
{

    public void saveEntity()
    {

        Logger logger = LoggerFactory.getLogger(MainClass.class);

        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring/config/BeanLocations.xml");

        StockBo stockBo = (StockBo) appContext.getBean("stockBo");

        /** insert **/
        Stock stock = new Stock();
        stock.setStockCode("7668");
        stock.setStockName("HAIO");
        StockDetail stockDetail = new StockDetail();
        stock.setStockDetail(stockDetail);
        stockDetail.setCompName("North");
        stockDetail.setRemark("Sails");
        Calendar instance = Calendar.getInstance();
        stockDetail.setListedDate(instance.getTime());
        stockDetail.setCompDesc("windsurf sail very ligth");
        stockDetail.setStock(stock);

        stockBo.save(stock);

        /** select **/
        Stock stock2 = stockBo.findByStockCode("7668");
        logger.info(stock2.getStockName());

        /** update **/
        stock2.setStockName("HAIO-1");
        stockBo.update(stock2);

        /** delete **/
        stockBo.delete(stock2);

        logger.info("Done");

    }

}
