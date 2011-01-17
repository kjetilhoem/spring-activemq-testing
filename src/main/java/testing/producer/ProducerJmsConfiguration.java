package testing.producer;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.core.JmsTemplate;

import testing.common.CommonJmsConfiguration;


@Configuration
@Import(CommonJmsConfiguration.class)
@ImportResource("producer-context.xml")
public class ProducerJmsConfiguration {
    
    @Autowired
    private ConnectionFactory connectionFactory;
    
    @Bean
    public JmsTemplate jmsTemplate() {
        final JmsTemplate template = new JmsTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
