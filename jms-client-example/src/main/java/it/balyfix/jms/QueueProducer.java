
package it.balyfix.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.log4j.Logger;

public class QueueProducer extends AbstractBrokerSupport {
	
    
    private static final Logger LOGGER = Logger.getLogger(QueueProducer.class);

    public static void main(String[] args) {
        QueueProducer producer = new QueueProducer();
        producer.run();
    }

    public void run() {
        Connection connection = null;
        try {
            connection = getConnectionFactory().createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue("QUEUE_NAME");
            MessageProducer producer = session.createProducer(destination);

            for (int i = 0; i < 1000; i++) {
                Message message = session.createTextMessage("Message # " + i);
                producer.send(message);
            }

            Message message = session.createTextMessage("done");
            producer.send(message);
        } catch (JMSException e) {
            LOGGER.error("Error occured while sending JMS messages", e);
        } finally {
            closeConnection(connection);
        }
    }
}