package testing.producer;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.Header;


/**
 * Gateway used by the producer to deliver events.
 */
public interface StatsReporter {
    
    @Gateway
    void reportSimpleEvent(String data,
            @Header(value="importantHeader", required=false) String importantHeaderValue);
    
}
