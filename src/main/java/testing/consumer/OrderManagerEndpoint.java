package testing.consumer;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import testing.common.CreateOrderRequest;
import testing.common.CreateOrderResponse;


@MessageEndpoint
public class OrderManagerEndpoint {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    @ServiceActivator
    // TODO add @Transactional?
    public CreateOrderResponse handle(CreateOrderRequest req) {
        logger.info("received request for creating a new order, id: " + req.orderId);
        // TODO try to persist in db, should fail if the orderId is duplicate etc..
        boolean success = true;
        return new CreateOrderResponse(req.orderId, success);
    }
    
}
