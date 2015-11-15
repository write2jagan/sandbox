
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

public class AsyncQueueConsumer extends AbstractBrokerSupport {

	private String queue_name;

	private String brokerUrl;

	public AsyncQueueConsumer(String queue_name, String brokerUrl) {
		super();
		this.queue_name = queue_name;
		this.brokerUrl = brokerUrl;
	}

	public AsyncQueueConsumer(String queue_name) {
		super();
		this.queue_name = queue_name;
	}

	private static final Logger LOGGER = Logger
			.getLogger(AsyncQueueConsumer.class);

	private final CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		String queueName = "testPersince";

		AsyncQueueConsumer consumer = new AsyncQueueConsumer(queueName);
		consumer.run();
	}

	public void run() {
		Connection connection = null;
		try {
			connection = getConnectionFactory().createConnection();

			final Session session = connection.createSession(true,
					Session.AUTO_ACKNOWLEDGE);

			Destination destination = session.createQueue(queue_name);
			MessageConsumer consumer = session.createConsumer(destination);

			consumer.setMessageListener(new MessageListener() {
				public void onMessage(Message message) {
					try {
						boolean trasacted = true;

						if (trasacted) {
							session.commit();
						} else {
							session.rollback();
						}

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