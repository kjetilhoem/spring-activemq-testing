package testing.common;

import no.fovea.core.api.emailaddress.ValidateEmailAddressRequest;
import no.fovea.core.api.emailaddress.ValidateEmailAddressResponse;
import no.fovea.core.api.emailaddress.ValidateEmailDomainRequest;
import no.fovea.core.api.emailaddress.ValidateEmailDomainResponse;


/**
 * Simple interface used by both the consumer & producer for case #1, synchronous request/reply. Note that
 * the consumer isn't really required to implement this interface at all, but it kind of makes sense. 
 */
public interface EmailValidator {
    
    ValidateEmailAddressResponse validateAddress(ValidateEmailAddressRequest req);
    
    ValidateEmailDomainResponse validateDomain(ValidateEmailDomainRequest req);
    
}
