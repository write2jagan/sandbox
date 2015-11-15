package it.balyfix.spring;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application implements MessageListener {

    public static void main(String[] args) throws JMSException {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
    }

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text = ((TextMessage) message).getText();
                if (text.contains("quit")) {
                    System.exit(0);
                }
            } catch (JMSException e) {
                e.printStackTrace();  
            }
        }
    }
}
