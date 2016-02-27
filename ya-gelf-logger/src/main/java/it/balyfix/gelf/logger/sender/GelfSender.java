package it.balyfix.gelf.logger.sender;

import it.balyfix.gelf.logger.message.GelfMessage;

public interface GelfSender {
	public static final int DEFAULT_PORT = 12201;

	public GelfSenderResult sendMessage(GelfMessage message);
	public void close();
}
