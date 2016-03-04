package it.balyfix.gelf.logger.providers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import it.balyfix.gelf.logger.commons.SenderConfiguration;
import it.balyfix.gelf.logger.sender.GelfKafkaSender;
import it.balyfix.gelf.logger.sender.GelfSender;

public class KafkaProvider extends AbstractGelfSenderProvider {

	public final static String TOPIC = "TOPIC";

	public final static String CLIENT_ID = "CLIENT_ID";

	@Override
	public boolean supports(String host) {

		return host.startsWith("kafka");
	}

	@Override
	public GelfSender create(SenderConfiguration configuration)
			throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {

		String clientId = (String) configuration.getExtraconfiguration().get(CLIENT_ID);
		String topic = (String) configuration.getExtraconfiguration().get(TOPIC);
		String host = getCanonicalHost(configuration.getHost());
		String bootstrapServer = host + ":" + configuration.getPort();
		return new GelfKafkaSender(topic, bootstrapServer, clientId);
	}

}
