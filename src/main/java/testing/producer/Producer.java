package testing.producer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;

import no.fovea.core.api.emailaddress.ValidateEmailAddressRequest;
import no.fovea.core.api.emailaddress.ValidateEmailAddressResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Producer {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    private int emailCounter;
    private int statCounter;
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private Topic[] statsTopics;
    
    @Autowired
    private Queue validateEmailAddressReplyQueue;
    
    
    /**
     * Synchronous request/reply implemented using spring-jms JmsTemplate directly, not pretty stuff, but
     * we could probably hide this stuff behind some generic facade, and use that facade behind typesafe/sound
     * interfaces?
     */
    //@Scheduled(fixedRate=5000)
    public void valdateAnEmailAddress() {
        jmsTemplate.convertAndSend("ValidateEmailAddress", createValidateEmailRequest(),
            new MessagePostProcessor() {
                    @Override public Message postProcessMessage(Message message) throws JMSException {
                        message.setJMSReplyTo(validateEmailAddressReplyQueue);
                        return message;
                    }
                });
        
        final ValidateEmailAddressResponse resp =
            (ValidateEmailAddressResponse)jmsTemplate.receiveAndConvert(validateEmailAddressReplyQueue);
        
        logger.info("valid email-address: " + resp.getCleanedEmailAddress());        
    }
    
    
    private ValidateEmailAddressRequest createValidateEmailRequest() {
        return new ValidateEmailAddressRequest()
            .withEmailAddress(emailCounter++ % 2 == 0
                ? "lars@hulte.net"
                : "krait@test.ings");
    }
    
    
    /**
     * Publishes a message to one of the topics, Stats.A/B
     */
    @Scheduled(fixedRate=10000)
    public void reportStats() {
        
        logger.info("running reportStats");
        
        jmsTemplate.send(statsTopics[statCounter++ % statsTopics.length], new MessageCreator() {
            @Override public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("someStat " + statCounter);
            }
        });
    }
}
