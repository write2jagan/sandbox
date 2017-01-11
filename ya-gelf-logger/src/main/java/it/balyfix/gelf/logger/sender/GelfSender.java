package it.balyfix.gelf.logger.sender;

import it.balyfix.gelf.logger.message.GelfMessage;

public interface GelfSender {
	
	public GelfSenderResult sendMessage(GelfMessage message);
	
	public void close();
}
