package testing.consumer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    
    /**
     * The fantastic echo-service =)
     */
    public String echo(String value) {
        logger.info("echoing '" + value + "'");
        return value;
    }
}
