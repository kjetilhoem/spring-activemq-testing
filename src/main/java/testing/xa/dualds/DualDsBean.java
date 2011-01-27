package testing.xa.dualds;

import java.sql.SQLException;
import java.util.List;


import com.atomikos.jdbc.AtomikosDataSourceBean;


public interface DualDsBean {

    void createDatabases() throws SQLException;

    AtomikosDataSourceBean getDs(int i);

    List<String[]> listContent(AtomikosDataSourceBean ds) throws SQLException;

    void ds1_works() throws SQLException;

    void ds1_rolls_back_on_failure() throws SQLException;

    void ds1_is_rolled_back_when_ds2_insert_fails() throws SQLException;

}
