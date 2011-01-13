package testing.common;

import javax.jms.ConnectionFactory;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonJmsConfiguration {
    
    @Bean
    public String brokerUrl() {
        return "tcp://localhost:61616";
    }
    
    
    @Bean
    public ConnectionFactory connectionFactory() {
        return new PooledConnectionFactory(createConnectionFactory());
    }
    
    
    private ActiveMQConnectionFactory createConnectionFactory() { // TODO Kjetil: the ConnectionFactory interface must probably be wrapped by some Atomikos-implementation
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl());
        // factory.setClientIDPrefix(clientIDPrefix)    TODO should this be set to appName perhaps?
        // TODO bunch of settings here...factory.setX.. check 'em out, see ActiveMQConnectionFactoryFactoryBean for the most important ones?
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
