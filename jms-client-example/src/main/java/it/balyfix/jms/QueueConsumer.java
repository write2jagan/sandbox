package it.balyfix.jms;

import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

public class QueueConsumer extends AbstractBrokerSupport {

    private static final String QUEUE_NAME = "skill.msg.balyfix";
    private static final Logger LOGGER = Logger.getLogger(AsyncQueueConsumer.class);

    private final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {
        QueueConsumer consumer = new QueueConsumer();
        consumer.run();
    }

    public void run() {
        Connection connection = null;
        try {
            connection = getConnectionFactory().createConnection();

            final Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(QUEUE_NAME);
            MessageConsumer consumer = session.createConsumer(destination);

            consumer.setMessageListener(new MessageListener() {
                public void onMessage(Message message) {
                    try {
                    	handle((TextMessage)message);
                        session.rollback();
                    } catch (JMSException e) {
                        e.printStackTrace(); 
                    }
                    throw new RuntimeException("Failed!");
                    
                }
            });

            connection.start();

            latch.await();
        } catch (Exception e) {
            LOGGER.error("Error occurred while receiving JMS messages", e);
        } finally {
            closeConnection(connection);
        }
    }

    private void handle(TextMessage message) {
        try {
            if ("done".equals(message.getText().toLowerCase())) {
                latch.countDown();
                LOGGER.info("Done processing exchanges");
            } else {
                LOGGER.info("Received: " + message.getText());
            }
        } catch (JMSException e) {
            LOGGER.error("Error while reading message", e);
        }
    }
}
