package it.balyfix.gelf.logger.providers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import it.balyfix.gelf.logger.commons.SenderConfiguration;
import it.balyfix.gelf.logger.sender.GelfSender;

public class KafkaProvider implements GelfSenderProvider {

	@Override
	public boolean supports(String host) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GelfSender create(SenderConfiguration configuration)
			throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
		// TODO Auto-generated method stub
		return null;
	}

}
