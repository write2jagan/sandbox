package it.balyfix.gelf.sender;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import it.balyfix.gelf.logger.commons.SenderConfiguration;
import it.balyfix.gelf.logger.providers.GelfSenderProvider;
import it.balyfix.gelf.logger.sender.GelfSender;

/**
 * @author fbalicchia
 * @version $Id: $
 */
public class GelfTestSenderProvider implements GelfSenderProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String host) {
		return host.startsWith("test");
	}

	@Override
	public GelfSender create(SenderConfiguration configuration)
			throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
		return new GelfTestSender();
	}

}
