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
        // logger.info("echoing '" + value + "'");
        return value;
    }
    
    
    /**
     * Wildcard topic-subscription..
     */
    public void eatStats(String stat) { // TODO can we do stuff like ', javax.jms.Message msg' do get hold of the actual message here??
        logger.info("stats: " + stat);
    }
}
