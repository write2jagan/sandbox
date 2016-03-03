package it.balyfix.gelf.test.sender;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import it.balyfix.gelf.logger.message.GelfMessage;
import it.balyfix.gelf.logger.sender.GelfSender;
import it.balyfix.gelf.logger.sender.GelfSenderResult;

public class GelfTestSender implements GelfSender {
	private static List<GelfMessage> messages = new CopyOnWriteArrayList<>();

	@Override
	public void close() {

	}

	public static List<GelfMessage> getMessages() {
		return messages;
	}

	@Override
	public GelfSenderResult sendMessage(GelfMessage message) {
		messages.add(message);
		return GelfSenderResult.OK;
	}
}
