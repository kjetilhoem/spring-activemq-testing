package testing.xa.dualds;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import no.fovea.test.JUnitUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DualDsXaTest {
    
    @Autowired
    private DualDsBean tester;
    
    
    @Before
    public void setUp() throws SQLException {
        tester.createDatabases();
    }
    
    @Test
    public void ds1_works() throws SQLException {
        tester.ds1_works();
        
        List<String[]> content = tester.listContent(tester.getDs(0));
        assertEquals(1, content.size());
        
        assertArrayEquals(
            new String[] {"abc","def"}, 
            content.get(0));
    }
    
    @Test
    public void ds1_rolls_back_on_failure() throws SQLException {        
        JUnitUtils.assertCheckedException(RuntimeException.class, "boom", new Callable<Void>() {
            @Override public Void call() throws Exception {
                tester.ds1_rolls_back_on_failure();
                return null;
            }
        });
        
        List<String[]> content = tester.listContent(tester.getDs(0));
        assertEquals(0, content.size());
    }
    
    @Test
    public void ds1_is_rolled_back_when_ds2_insert_fails() throws SQLException {
        JUnitUtils.assertCheckedException(RuntimeException.class, "boom", new Callable<Void>() {            
            @Override public Void call() throws Exception {
                tester.ds1_is_rolled_back_when_ds2_insert_fails();
                return null;
            }
        });
        
        List<String[]> content1 = tester.listContent(tester.getDs(0));
        assertEquals(0, content1.size());

        List<String[]> content2 = tester.listContent(tester.getDs(1));
        assertEquals(0, content2.size());
    }
    
}
