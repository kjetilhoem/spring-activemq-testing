package testing.common;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

import no.fovea.core.spring.config.MarshallerConfiguration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.oxm.Marshaller;


@Configuration
@Import(MarshallerConfiguration.class)
public class CommonJmsConfiguration {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    @Value("#{systemProperties['JmsClientId']}")
    private String jmsClientId;
    
    @Autowired
    private Marshaller coreApiMarshaller;
    
    
    @Bean
    public String brokerUrl() {
        return "tcp://localhost:61616";
    }
    
    
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PooledConnectionFactory(createConnectionFactory());
    }
    
    
    private ActiveMQConnectionFactory createConnectionFactory() {
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl());
        if (jmsClientId != null) {
            logger.info("setting JMS ClientID to '" + jmsClientId + "'");
            factory.setClientID(jmsClientId);
        }
        // TODO check out the setters one this one.. probably something interesting here.. see ActiveMQConnectionFactoryFactoryBean for the most important ones?
        return factory;
    }
    
    
    @Bean
    public Topic statsTopicA() {
        return new ActiveMQTopic("Stats.A");
    }
    
    
    @Bean
    public Topic statsTopicB() {
        return new ActiveMQTopic("Stats.B");
    }
    
    
    @Bean
    public MessageConverter coreApiMarshallingMessageConverter() {
        return new MarshallingMessageConverter(coreApiMarshaller);        
    }
}
