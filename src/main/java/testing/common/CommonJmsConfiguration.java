package testing.common;

import javax.jms.ConnectionFactory;

import no.fovea.core.spring.config.MarshallerConfiguration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
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
    
    @Value("#{systemProperties['useXaConnectionFactory']}")
    private Boolean useXaConnectionFactory;
    
    @Autowired
    private Marshaller coreApiMarshaller;
    
    
    @Bean
    public String brokerUrl() {
        return "tcp://localhost:61616";
    }
    
    
    @Bean
    public ConnectionFactory connectionFactory() {
        return Boolean.TRUE.equals(useXaConnectionFactory)
            ? createXaConnectionFactory()
            : createPooledConnectionFactory();
    }
    
    
    private ConnectionFactory createPooledConnectionFactory() {
        final PooledConnectionFactory pooledFactory = new PooledConnectionFactory(createConnectionFactory());
        pooledFactory.setMaxConnections(10);
        pooledFactory.setIdleTimeout(30000);
        return pooledFactory;
    }
    
    
    private ConnectionFactory createXaConnectionFactory() {
        throw new RuntimeException("not implemented");
    }
    
    
    private ActiveMQConnectionFactory createConnectionFactory() {
        return new ActiveMQConnectionFactory(brokerUrl());
    }
    
    
    @Bean
    public MessageConverter coreApiMarshallingMessageConverter() {
        return new MarshallingMessageConverter(coreApiMarshaller);        
    }
}
