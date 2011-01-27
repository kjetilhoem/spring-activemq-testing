package testing.consumer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

@Configuration
@ImportResource("xa-context.xml")
public class ConsumerJdbcConfiguration {
    private final Logger logger = Logger.getLogger(getClass());

    private final static boolean RECREATE = false;

    @Bean
    public DataSource embeddedDataSource() throws SQLException {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        
        MysqlXADataSource xads = new MysqlXADataSource();
        xads.setDatabaseName("xatest");
        xads.setCreateDatabaseIfNotExist(true);
        xads.setPassword("dev");
        xads.setUser("root");        
        xads.setLogXaCommands(true);

        ds.setXaDataSource(xads);
        
        ds.setPoolSize(1);                
        ds.setUniqueResourceName("XA_DATA_SOURCE");        
        
        Connection conn = null;
        try {
            conn = ds.getConnection();
            Statement s = conn.createStatement();

            if (RECREATE) {
                logger.info("RECREATE");
                s.executeUpdate("drop table order_shmorder");
            }
            
            if (!tableExists(s, "order_shmorder")) {            
                logger.info("No table. Creating...");
                
                s.executeUpdate("create table order_shmorder (id varchar(45) unique, comment varchar(200)) engine = InnoDB");
                logger.info("Created table order_schmorder");
            }
            
            s.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        
        
        return ds;
    }

    private boolean tableExists(Statement s, String tableName) {
        try {
            s.executeQuery("select 1 from " + tableName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
}
