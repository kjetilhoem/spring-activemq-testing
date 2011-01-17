package testing.consumer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
    // TODO remove me
    
    private final Logger logger = Logger.getLogger(getClass());
    
    
    /**
     * Wildcard topic-subscription on Stats.>
     */
    public void eatStats(String stat) {
        logger.info("stats: " + stat);
    }
}
