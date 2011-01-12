package testing.producer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.SessionCallback;
import org.springframework.jms.support.JmsUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


// TODO this stuff is crap, delux.. should be reimplemented on top of spring-integration's JMS-support, should
// be a lot cleaner then...
@Component
public class Producer {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    private int echoCounter;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    
    /**
     * Synchronous request/reply implemented using spring-jms JmsTemplate directly, not pretty stuff, but
     * we could probably hide this stuff behind some generic facade, and use that facade behind typesafe/sound
     * interfaces?
     */
    @Scheduled(fixedRate=500)
    public void echoSomething() {
        
        final String result = jmsTemplate.execute(new SessionCallback<String>() {
            @Override public String doInJms(Session session) throws JMSException {
                final TemporaryQueue replyQueue = session.createTemporaryQueue();
                MessageConsumer consumer = null;
                MessageProducer producer = null;
                try {
                    consumer = session.createConsumer(replyQueue);
                    producer = session.createProducer(session.createQueue("Echo"));
                    
                    final TextMessage msg = session.createTextMessage(echoCounter++ % 2 == 0
                        ? "echo this"
                        : "echo that");
                    msg.setJMSReplyTo(replyQueue);

                    producer.send(msg);
                    final Message reply = consumer.receive(2500);
                    return reply == null
                        ? null
                        : ((TextMessage)reply).getText();
                } finally {
                    JmsUtils.closeMessageProducer(producer);
                    JmsUtils.closeMessageConsumer(consumer);
                    replyQueue.delete();
                }
            }
        }, true);
        
        logger.info("received reply: " + result);        
    }
    
    
    // TODO pubSub-stuff please...
}
