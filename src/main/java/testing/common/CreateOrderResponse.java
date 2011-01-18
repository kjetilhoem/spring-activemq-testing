package testing.common;

import java.io.Serializable;


public class CreateOrderResponse implements Serializable {

    private static final long serialVersionUID = -7364226163356511075L;
    
    public final String orderId;
    public final boolean success;
    
    public CreateOrderResponse(String orderId, boolean success) {
        this.orderId = orderId;
        this.success = success;
    }
}
