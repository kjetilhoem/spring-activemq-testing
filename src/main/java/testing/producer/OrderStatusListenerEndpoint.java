package testing.producer;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import testing.common.CreateOrderResponse;


@MessageEndpoint
public class OrderStatusListenerEndpoint {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    
    @ServiceActivator
        // TODO @Transactional
    public void handle(CreateOrderResponse resp) {
        
        // TODO verify that the db-order is pending
        
        if (resp.success) {
            logger.info("successfully created order " + resp.orderId);
            // TODO update db-order
        } else {
            logger.warn("order " + resp.orderId + " wasn't created");
            // TODO delete db-order
        }
        
        // TODO if any operation here fails, db & message should roll back
    }
}
