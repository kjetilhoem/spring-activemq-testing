package testing.consumer;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import testing.common.CommonJmsConfiguration;


@Configuration
@Import(CommonJmsConfiguration.class)
@ImportResource("consumer-context.xml") // message-driven consumers defined in xml..
public class ConsumerJmsConfiguration {
    
    @Bean(destroyMethod="stop")
    public BrokerService embeddedBroker() throws Exception {
        final BrokerService broker = BrokerFactory.createBroker("broker:tcp://localhost:61616");
        broker.setBrokerName("embedded-test-broker");
        broker.setPersistent(false);
        broker.start();
        return broker;
    }
}
