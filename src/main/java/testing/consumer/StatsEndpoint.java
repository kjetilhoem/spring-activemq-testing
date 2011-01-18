package testing.consumer;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Header;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;


@MessageEndpoint
public class StatsEndpoint {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    @ServiceActivator
    public void handleSimpleStats(String data,
            @Header(value="importantHeader", required=false) String importantHeader) {
        logger.info("receieved a stats-event, " + data
            + ((importantHeader == null)
                ? ""
                : (", with important-header: " + importantHeader)));
    }
}
