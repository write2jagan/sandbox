package it.balyfix.jms;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

public abstract class AbstractBrokerSupport {

    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final Logger LOGGER = Logger.getLogger(AbstractBrokerSupport.class);

    private final ConnectionFactory connectionFactory;

    public AbstractBrokerSupport() {
        super();
        this.connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
    }
    
    public AbstractBrokerSupport(String broker_url) {
        super();
        this.connectionFactory = new ActiveMQConnectionFactory(broker_url);
    }
    

    protected ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    /**
     * Stop and close the JMS connection
     *
     * @param connection
     */
    protected void closeConnection(Connection connection) {
        try {
            connection.stop();
            connection.close();
        } catch (JMSException e) {
            LOGGER.warn("Error closing JMS connection");
        }
    }
}
