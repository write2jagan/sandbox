
package it.balyfix.jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.log4j.Logger;

public class TopicPublisher extends AbstractBrokerSupport {

    private static final String TOPIC_NAME = "news.it.software";
    private static final Logger LOGGER = Logger.getLogger(TopicPublisher.class);

    public static void main(String[] args) {
        TopicPublisher publisher = new TopicPublisher();
        publisher.run();
    }

    public void run() {
        Connection connection = null;
        try {
            connection = getConnectionFactory().createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(destination);

                Message message = session.createTextMessage("Helo o newyddion TG");
                message.setStringProperty("Language", "cy");
                producer.send(message);

                message = session.createTextMessage("Dia duit � IT nuachta");
                message.setStringProperty("Language", "ga");
                producer.send(message);

                message = session.createTextMessage("Hello from IT News!");
                message.setStringProperty("Language", "en");
                producer.send(message);

        } catch (JMSException e) {
            LOGGER.error("Error occured while sending JMS messages", e);
        } finally {
            closeConnection(connection);
        }
    }
}