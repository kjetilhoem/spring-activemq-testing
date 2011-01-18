package testing.consumer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import no.fovea.core.api.emailaddress.ValidateEmailAddressRequest;
import no.fovea.core.api.emailaddress.ValidateEmailAddressResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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
    
    
    @Autowired
    private DataSource dataSource;
    
    
    /**
     * Wildcard topic-subscription on Stats.>
     */
    @Transactional
    public void eatStats(String stat) { // TODO can we do stuff like ', javax.jms.Message msg' do get hold of the actual message here??

        checkTables();
        
        Connection conn = null;
        
        try {
            conn = dataSource.getConnection();
            logger.info("DataSource class: " + dataSource.getClass());
            Statement s = conn.createStatement();
            s.executeUpdate("update stat set val='"+stat+"' where id=100");
            
            logger.info("Current contents of table 'stat':");            
            ResultSet rs = s.executeQuery("select * from stat");
            while (rs.next()) {
                logger.info("Row: " + rs.getString(1) + ", " + rs.getString(2));
            }
            
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
        
        logger.info("stats: " + stat);
    }
    
    
    public void checkTables() {
        logger.info("checking tables...");
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement s = conn.createStatement();
            try {
                s.executeQuery("select * from stat");
                logger.info("table already there.");
                
            } catch (SQLException e) { //table not there => create it
                logger.info("Creating stat table...");
                s.executeUpdate("create table stat (id numeric(11), val varchar(20))");
                s.executeUpdate("insert into stat values (100, 'hei')");
                ResultSet rs = s.executeQuery("select * from stat");
                while (rs.next()) {
                    logger.info("Row: " + rs.getString(1) + ", " + rs.getString(2));
                }
            }
            
            s.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }


    private void closeConnection(Connection conn) {
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

}
