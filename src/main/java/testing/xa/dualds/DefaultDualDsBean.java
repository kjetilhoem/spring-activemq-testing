package testing.xa.dualds;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import com.atomikos.jdbc.AtomikosDataSourceBean;

public class DefaultDualDsBean implements DualDsBean {

    
    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private AtomikosDataSourceBean[] dataSources;

    @Transactional
    public void insert(AtomikosDataSourceBean ds) throws SQLException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            Statement s = conn.createStatement();
            s.executeUpdate("insert into test_table (id, comment) values ('abc', 'def')");            
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    @Transactional
    public void ds1_works() throws SQLException {
        insert(dataSources[0]);
    }

    @Override
    @Transactional
    public void ds1_rolls_back_on_failure() throws SQLException {
        insert(dataSources[0]);
        throw new RuntimeException("boom");
    }

    @Override
    @Transactional
    public void ds1_is_rolled_back_when_ds2_insert_fails() throws SQLException {
        insert(dataSources[0]);
        insert(dataSources[1]);
        throw new RuntimeException("boom");
    }
    
    @Override
    public AtomikosDataSourceBean getDs(int i) {
        return dataSources[i];
    }
    
    @Override
    public void createDatabases() throws SQLException {
        for (AtomikosDataSourceBean ds : dataSources) {
            System.out.println("Creating db in datasource "+ds.getUniqueResourceName());
            createTestDb(ds);
        }
    }
    
    private void createTestDb(AtomikosDataSourceBean ds) throws SQLException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            Statement s = conn.createStatement();

            if (tableExists(s, "test_table")) {
                s.executeUpdate("drop table test_table");
            }
            
            s.executeUpdate("create table test_table (id varchar(45) unique, comment varchar(200))");
            
            s.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
    

    private boolean tableExists(Statement s, String tableName) {
        try {
            s.executeQuery("select * from " + tableName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<String[]> listContent(AtomikosDataSourceBean ds) throws SQLException {
        
        logger.info("Listing content:");
        
        Connection conn = null;
        try {
            conn = ds.getConnection();
            
            final List<String[]> result = new ArrayList<String[]>();
            final Statement s = conn.createStatement();
            final ResultSet rs = s.executeQuery("select * from test_table");            
            final int columnCount = rs.getMetaData().getColumnCount();            
            
            while (rs.next()) {                
                final String[] row = new String[columnCount];
                result.add(row);
                
                for (int col=0; col<columnCount; col++) {
                    row[col] = rs.getString(col + 1);
                }
                
                logger.info("Row: " + Arrays.toString(row));
            }
            
            s.close();
            
            return result;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
