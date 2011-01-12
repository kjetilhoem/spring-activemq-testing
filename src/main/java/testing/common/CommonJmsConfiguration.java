package testing.common;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CommonJmsConfiguration {
    
    @Bean
    public ConnectionFactory connectionFactory() {  // TODO Kjetil: the ConnectionFactory interface must probably be wrapped by some Atomikos-implementation
        final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616"); // TODO failover, handled by spring?
        // factory.setClientIDPrefix(clientIDPrefix)    TODO should this be set to appName perhaps?
        // TODO bunch of settings here...factory.setX.. check 'em out, see ActiveMQConnectionFactoryFactoryBean for the most important ones?
        return factory;
    }
    
    // TODO JmsTemplate?
}
