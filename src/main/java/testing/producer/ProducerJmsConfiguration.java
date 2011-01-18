package testing.producer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import testing.common.CommonJmsConfiguration;


@Configuration
@Import(CommonJmsConfiguration.class)
@ImportResource("producer-context.xml")
public class ProducerJmsConfiguration {
    
}
