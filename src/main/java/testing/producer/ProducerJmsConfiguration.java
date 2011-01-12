package testing.producer;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import testing.common.CommonJmsConfiguration;


@Configuration
@Import(CommonJmsConfiguration.class)
public class ProducerJmsConfiguration {
    
    // TODO template & crap?

}
