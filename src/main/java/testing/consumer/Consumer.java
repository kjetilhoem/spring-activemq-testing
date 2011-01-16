package testing.consumer;

import no.fovea.core.api.emailaddress.ValidateEmailAddressRequest;
import no.fovea.core.api.emailaddress.ValidateEmailAddressResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    
    /**
     * Mock-implementation of core-api's EmailAddressService.validateEmailAddress(). Listens on a queue &
     * responds to the ReplyTo queue.
     */
    public ValidateEmailAddressResponse validateEmailAddress(ValidateEmailAddressRequest req) {
        logger.info("validating: " + req.getEmailAddress());
        return new ValidateEmailAddressResponse().withCleanedEmailAddress(req.getEmailAddress());
    }
    
    
    /**
     * Wildcard topic-subscription on Stats.>
     */
    public void eatStats(String stat) {
        logger.info("stats: " + stat);
    }
}
