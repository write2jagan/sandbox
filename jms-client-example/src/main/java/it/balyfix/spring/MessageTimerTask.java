
package it.balyfix.spring;

import java.util.Date;
import java.util.TimerTask;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


public class MessageTimerTask extends TimerTask implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(MessageTimerTask.class);

    private JmsTemplate template;

    private boolean enabled = true;

    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }

    @Override
    public void run() {
        if (enabled) {
            template.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    String message = String.format("Message sent at %tc", new Date());
                    return session.createTextMessage(message);
                }
            });
        }
    }

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String command = ((TextMessage) message).getText();
                if (command.contains("timer")) {
                    onTimerCommand(command);
                } 
            }
        } catch (JMSException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void onTimerCommand(String command) {
        if (command.contains("on")) {
            LOGGER.info("Timer is now on");
            enabled = true;
        } else {
            LOGGER.info("Timer is now off");
            enabled = false;
        }
    }
}
