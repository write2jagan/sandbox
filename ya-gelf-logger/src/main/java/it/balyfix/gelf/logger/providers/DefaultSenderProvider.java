package it.balyfix.gelf.logger.providers;

import java.io.IOException;

import it.balyfix.gelf.logger.commons.SenderConfiguration;
import it.balyfix.gelf.logger.sender.GelfSender;
import it.balyfix.gelf.logger.sender.GelfTCPSender;
import it.balyfix.gelf.logger.sender.GelfUDPSender;

public class DefaultSenderProvider extends AbstractGelfSenderProvider {

	@Override
	public boolean supports(String host) {
		return host.startsWith("tcp:") || host.startsWith("udp:");
	}

	@Override
	public GelfSender create(SenderConfiguration configuration) throws IOException {

		String host = configuration.getHost();

		Integer port = GelfSender.DEFAULT_PORT;

		if (configuration.getPort() != null) {
			port = Integer.valueOf(configuration.getPort());
		}

		if (host.startsWith("tcp:")) {
			return new GelfTCPSender(getCanonicalHost(host), port);

		} else if (host.startsWith("udp:")) {
			return new GelfUDPSender(getCanonicalHost(host), port);
		}

		return null;
	}

}
