package it.balyfix.gelf.logger.providers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import it.balyfix.gelf.logger.commons.SenderConfiguration;
import it.balyfix.gelf.logger.sender.GelfKafkaSender;
import it.balyfix.gelf.logger.sender.GelfSender;

public class KafkaProvider implements GelfSenderProvider {

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
		String[] hostSplitter = configuration.getHost().split(":");
		if (hostSplitter.length != 2) {
			throw new IllegalArgumentException("Please check host configuration! I'm wainting kafka:<ipaddress> I retrieve : " + configuration.getHost());
		}
		String host = configuration.getHost().split(":")[1];
		String bootstrapServer = host + ":" + configuration.getPort();
		return new GelfKafkaSender(topic, bootstrapServer, clientId);
	}

}
