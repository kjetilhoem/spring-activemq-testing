package testing.producer;

import java.util.concurrent.TimeUnit;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;

import testing.common.CommonJmsConfiguration;


@Configuration
@Import(CommonJmsConfiguration.class)
@ImportResource("producer-context.xml")
public class ProducerJmsConfiguration {
    
    @Autowired
    private ConnectionFactory connectionFactory;
    
    @Autowired
    private MessageConverter messageConverter;
    
    @Bean
    public JmsTemplate jmsTemplate() {
        final JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setReceiveTimeout(TimeUnit.SECONDS.toMillis(10));
        return template;
    }
    
    
    @Bean
    public Queue validateEmailAddressReplyQueue() {
        return new ActiveMQQueue("ValidateEmailAddressReply");
    }
}
