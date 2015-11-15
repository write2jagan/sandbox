package it.balyfix.spring;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;


public class MessageReceiver implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(MessageReceiver.class);

    private boolean echo = true;

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String text = ((TextMessage) message).getText();
                if (text.contains("echo")) {
                    onEchoCommand(text);
                } else {
                    doEcho(text);
                }
            }
        } catch (JMSException e) {
            LOGGER.warn("Error receiving JMS message", e);
        }
    }

    private void onEchoCommand(String text) {
        echo = text.contains("on");
        LOGGER.info(String.format("Echo is now %s", echo ? "on" : "off"));
    }

    private void doEcho(String text) {
        if (echo) {
            LOGGER.info(text);
        }
    }
}
