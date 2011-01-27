package testing.consumer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.transaction.annotation.Transactional;

import testing.common.CreateOrderRequest;
import testing.common.CreateOrderResponse;


@MessageEndpoint
public class OrderManagerEndpoint {
    
    private final Logger logger = Logger.getLogger(getClass());
    
    @Autowired
    private DataSource dataSource;

    
    @Autowired
    private MessageChannel newOrderResponseChannel;
    
    
    private Random random = new Random();
    
    
    @Transactional
    @ServiceActivator
    public void handle(CreateOrderRequest req) throws SQLException {
        logger.info("received request for creating a new order, id: " + req.orderId);
        
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement s = conn.createStatement();
            
            s.executeUpdate("insert into order_shmorder (id, comment) values ('" + req.orderId + "', 'Created @ "+new Date()+"')");
            displayTable(s, "order_shmorder");
                
            s.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        boolean success = true;

        newOrderResponseChannel.send(MessageBuilder.withPayload(new CreateOrderResponse(req.orderId, success)).build());
        
        //if (random.nextBoolean()) {
        if (req.orderId.contains("fail")) {
            throw new RuntimeException("\"unexpected\" failure past connection.close()");
        }
    }



    private void displayTable(Statement s, String tableName) throws SQLException {
        logger.info("Current contents of table '" + tableName + "':");
        
        ResultSet rs = s.executeQuery("select * from " + tableName);
        int row = 0;
        while (rs.next()) {
            StringBuilder sb = new StringBuilder();
            sb.append("result row #" + (row++) + ": {");
            for (int col = 1; col <= rs.getMetaData().getColumnCount(); col++) {
                sb.append(" " + rs.getString(col) + " ");
            }
            sb.append("}");
            logger.info(sb.toString());
        }
    }
    
}
