package it.balyfix.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

public class TopicSubscriber extends AbstractBrokerSupport {

    private static final String TOPIC_NAME = "news.f.software";
    private static final Logger LOGGER = Logger.getLogger(QueueConsumer.class);

    public static void main(String[] args) {
        TopicSubscriber subscriber = new TopicSubscriber();
        subscriber.run();
    }

    public void run() {
        Connection connection = null;
        try {
            connection = getConnectionFactory().createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(TOPIC_NAME);
            MessageConsumer consumer = session.createConsumer(destination);

            boolean done = false;

            while (!done) {
                TextMessage message = (TextMessage) consumer.receive();
                if ("done".equals(message.getText().toLowerCase())) {
                    LOGGER.info("Done receiving messages!");
                    done = true;
                } else {
                    LOGGER.info("Received: " + message.getText());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while receiving JMS messages", e);
        } finally {
            closeConnection(connection);
        }
    }
}
