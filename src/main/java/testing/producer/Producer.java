package testing.producer;

import no.fovea.core.api.emailaddress.ValidateEmailAddressRequest;
import no.fovea.core.api.emailaddress.ValidateEmailAddressResponse;
import no.fovea.core.api.emailaddress.ValidateEmailDomainRequest;
import no.fovea.core.api.emailaddress.ValidateEmailDomainResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessageTimeoutException;
import org.springframework.stereotype.Component;

import testing.common.EmailValidator;


@Component
public class Producer {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    /**
     * This is a spring-integration gateway, proxied interface, case #1.
     */
    @Autowired
    private EmailValidator emailValidator;
    
    
    public void validateEmailAddress(String address) {
        try {
            final ValidateEmailAddressResponse resp = emailValidator.validateAddress(
                new ValidateEmailAddressRequest().withEmailAddress(address));
            
            if (resp == null) {
                logger.warn("unable to validate email...");
            } else {
                logger.info("email validated, resp: " + resp.getCleanedEmailAddress());
            }
        } catch (MessageTimeoutException e) {
            logger.info("timed out while waiting for validation-response", e);
        }
    }
    
    
    public void validateEmailDomain(String address) {
        final ValidateEmailDomainResponse resp = emailValidator.validateDomain(
            new ValidateEmailDomainRequest().withEmailAddress(address));
        
        if (resp == null) {
            logger.warn("unable to validate domain...");
        } else {
            logger.info("domain validated, resp: " + resp.getCleanedEmailAddress());
        }
    }
}
