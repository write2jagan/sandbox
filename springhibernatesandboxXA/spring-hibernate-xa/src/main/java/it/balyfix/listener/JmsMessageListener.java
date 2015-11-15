package it.balyfix.listener;

import java.util.Calendar;

import it.balyfix.stock.bo.StockBo;
import it.balyfix.stock.model.Stock;
import it.balyfix.stock.model.StockDetail;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class JmsMessageListener implements MessageListener {

	@Autowired
	private StockBo stockBo;

	Logger logger = LoggerFactory.getLogger(JmsMessageListener.class);

	@Override
	public void onMessage(Message message) {
		logger.info("message: " + message.getClass());

		if (message instanceof TextMessage) {
			try {
				
				TextMessage textMsg = (TextMessage) message;
				logger.info("         " + textMsg.getText());

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

			} catch (JMSException ex) {
				ex.printStackTrace();
			}
		}

	}

}
