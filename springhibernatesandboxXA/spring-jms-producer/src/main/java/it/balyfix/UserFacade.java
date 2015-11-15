package it.balyfix;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;


public class UserFacade
{

    private JmsTemplate jmsTemplate;

    private Queue queue;

    public UserFacade()
    {
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueue(Queue queue)
    {
        this.queue = queue;
    }

    private static AtomicInteger counter = new AtomicInteger(0);

    public void createUser(String name, int count) throws JMSException
    {
        for (int i = 0; i < count; i++)
        {
            createUser(name + "-" + i);
        }
    }

    public void createUser(String name) throws JMSException
    {
        int currentCount = counter.addAndGet(1);

        final String text = name + " / " + currentCount;

        this.jmsTemplate.send(this.queue, new MessageCreator()
        {

            @Override
            public Message createMessage(Session session) throws JMSException
            {
                return session.createTextMessage(text);
            }
        });

        if (currentCount > 0 && currentCount % 100 == 0)
        {
            System.out.println("executed: " + currentCount);
        }
    }

}