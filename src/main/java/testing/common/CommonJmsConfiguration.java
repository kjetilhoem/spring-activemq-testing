package testing.common;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonJmsConfiguration {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    @Value("#{systemProperties['JmsClientId']}")
    private String jmsClientId;
    
    
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
}
