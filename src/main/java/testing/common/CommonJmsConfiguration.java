package testing.common;

import javax.jms.ConnectionFactory;

import no.fovea.core.spring.config.MarshallerConfiguration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.oxm.Marshaller;

import com.atomikos.diagnostics.Console;
import com.atomikos.jms.AtomikosConnectionFactoryBean;


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
        final PooledConnectionFactory pooledFactory = new PooledConnectionFactory(
            new ActiveMQConnectionFactory(brokerUrl()));
        
        pooledFactory.setMaxConnections(10);
        pooledFactory.setIdleTimeout(30000);
        return pooledFactory;
    }
    

    public class DevNullConsole implements Console {
        @Override public void println(String string) {}
        @Override public void print(String string) {}
        @Override public void println(String string, int level) {}
        @Override public void print(String string, int level) {}
        @Override public void close() {}
        @Override public void setLevel(int level) {}
        @Override public int getLevel() { return 0; }
    }

    
    private ConnectionFactory createXaConnectionFactory() {
        
        /*
        com.atomikos.icatch.system.Configuration.removeConsoles();
        com.atomikos.icatch.system.Configuration.addConsole(new DevNullConsole());
        */
        
        AtomikosConnectionFactoryBean cf = new AtomikosConnectionFactoryBean();
        
        cf.setXaConnectionFactory(new ActiveMQXAConnectionFactory(brokerUrl()));
        cf.setUniqueResourceName("XA_MESSAGE_BROKER");        
        cf.setMaxPoolSize(10);
        
        return cf;
    }
    

    @Bean
    public MessageConverter coreApiMarshallingMessageConverter() {
        return new MarshallingMessageConverter(coreApiMarshaller);        
    }
}
