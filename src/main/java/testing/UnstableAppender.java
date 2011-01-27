package testing;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.spi.LoggingEvent;


public class UnstableAppender extends ConsoleAppender {

    private static final int LIMIT = 2;
    private static int hits = LIMIT;

    @Override
    public void append(LoggingEvent event) {
        super.append(event);
        
        if (event.getMessage().toString().contains("XAResource.commit")) {
            if (--hits == 0) {
                Runtime.getRuntime().halt(1);
            }
        }
    }
    
}
