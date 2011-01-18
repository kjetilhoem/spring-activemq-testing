package testing.common;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.jms.ConnectionFactory;

import no.fovea.core.spring.config.MarshallerConfiguration;
import no.fovea.reflection.ProxyFactory;

import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.oxm.Marshaller;

import com.atomikos.jms.AtomikosConnectionFactoryBean;


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
    public AtomikosConnectionFactoryBean connectionFactory() {
        
        AtomikosConnectionFactoryBean cf = new AtomikosConnectionFactoryBean();
        
        cf.setXaConnectionFactory(createConnectionFactory());
        cf.setUniqueResourceName("XA_BROKER");
        
        cf.setMaxPoolSize(10);
        //cf.setBorrowConnectionTimeout(60);
        
        return cf;
        
        //return new PooledConnectionFactory(createConnectionFactory());
    }
    
    
    private ActiveMQXAConnectionFactory createConnectionFactory() {
        final ActiveMQXAConnectionFactory factory = new ActiveMQXAConnectionFactory(brokerUrl());        
        if (jmsClientId != null) {
            logger.info("setting JMS ClientID to '" + jmsClientId + "'");
            //factory.setClientID(jmsClientId);
            //factory.setClientIDPrefix(jmsClientId);
        }
        
        return factory;
    }
    
    
    @Bean
    public MessageConverter coreApiMarshallingMessageConverter() {
        return new MarshallingMessageConverter(coreApiMarshaller);        
    }
}
