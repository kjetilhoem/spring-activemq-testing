package testing.common;

import java.io.Serializable;


public class CreateOrderRequest implements Serializable {
    
    private static final long serialVersionUID = 1195225223336690584L;
    
    public final String orderId;
    
    public CreateOrderRequest(String orderId) {
        this.orderId = orderId;
    }
}
