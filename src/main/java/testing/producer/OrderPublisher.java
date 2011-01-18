package testing.producer;

import org.springframework.integration.annotation.Gateway;

import testing.common.CreateOrderRequest;


/**
 * Gateway used by the producer to create orders.
 */
public interface OrderPublisher {
    
    @Gateway
    void createOrder(CreateOrderRequest orderRequest);

}
