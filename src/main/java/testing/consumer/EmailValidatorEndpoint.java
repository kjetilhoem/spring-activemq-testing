package testing.consumer;

import no.fovea.core.api.emailaddress.ValidateEmailAddressRequest;
import no.fovea.core.api.emailaddress.ValidateEmailAddressResponse;
import no.fovea.core.api.emailaddress.ValidateEmailDomainRequest;
import no.fovea.core.api.emailaddress.ValidateEmailDomainResponse;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import testing.common.EmailValidator;


@MessageEndpoint("emailValidator")
public class EmailValidatorEndpoint implements EmailValidator {
    
    private final Logger logger = Logger.getLogger(getClass());

    @Override
    @ServiceActivator
    public ValidateEmailAddressResponse validateAddress(ValidateEmailAddressRequest req) {
        logger.info("validating address: " + req.getEmailAddress());
        if ("timeout".equals(req.getEmailAddress())) {
            logger.info("forcing a timeout...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }       
        // TODO add failure-stuff + errorChannel??
        return new ValidateEmailAddressResponse().withCleanedEmailAddress(req.getEmailAddress());
    }

    @Override
    @ServiceActivator
    public ValidateEmailDomainResponse validateDomain(ValidateEmailDomainRequest req) {
        logger.info("validating domain for address: " + req.getEmailAddress());
        return new ValidateEmailDomainResponse().withCleanedEmailAddress(req.getEmailAddress());
    }
}
