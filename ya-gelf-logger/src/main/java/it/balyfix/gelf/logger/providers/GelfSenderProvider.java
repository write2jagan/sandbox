package it.balyfix.gelf.logger.providers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import it.balyfix.gelf.logger.commons.SenderConfiguration;
import it.balyfix.gelf.logger.sender.GelfSender;

public interface GelfSenderProvider {

	/**
	 * 
	 * @param host
	 *            the host string
	 * @return true if the host scheme/pattern/uri is supported by this
	 *         provider.
	 */
	boolean supports(String host);

	/**
	 * Create the sender based on the passed configuration.
	 * 
	 * @param configuration
	 *            the sender configuration
	 * @return GelfSender instance.
	 * @throws IOException
	 *             if there is an error in the underlying protocol
	 */
	GelfSender create(SenderConfiguration configuration)
			throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException;
}
