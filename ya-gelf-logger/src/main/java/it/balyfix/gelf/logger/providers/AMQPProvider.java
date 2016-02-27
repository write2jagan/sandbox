package it.balyfix.gelf.logger.providers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import it.balyfix.gelf.logger.commons.SenderConfiguration;
import it.balyfix.gelf.logger.sender.GelfAMQPSender;
import it.balyfix.gelf.logger.sender.GelfSender;

public class AMQPProvider implements GelfSenderProvider {

	public static String AMQP_EXCHANGE_NAME = "AMQP_EXCHANGE_NAME";

	public static String AMQP_ROUTING_KEY = "AMQPR_OUTING_KEY";

	public static String MAX_RETRIES = "MAX_RETRIES";

	@Override
	public boolean supports(String host) {

		return host.contains("amqp:");
	}

	@Override
	public GelfSender create(SenderConfiguration configuration)
			throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {

		String amqpExchangeName = (String) configuration.getExtraconfiguration().get(AMQP_EXCHANGE_NAME);
		String amqpRoutingKey = (String) configuration.getExtraconfiguration().get(AMQP_ROUTING_KEY);
		Integer maxRetries = (Integer) configuration.getExtraconfiguration().get(MAX_RETRIES);
		return new GelfAMQPSender(configuration.getHost(), amqpExchangeName, amqpRoutingKey, maxRetries);

	}

}
