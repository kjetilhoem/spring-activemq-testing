package testing.consumer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    
    /**
     * The fantastic echo-service =), p2p, listens on a queue & responds to the ReplyTo queue
     */
    public String echo(String value) {
        // logger.info("echoing '" + value + "'");
        return value;
    }
    
    
    /**
     * Wildcard topic-subscription on Stats.>
     */
    public void eatStats(String stat) {
        logger.info("stats: " + stat);
    }
}
