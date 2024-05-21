package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestForeignScan extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestForeignScan.class);
    private static final String expectedPlanType = "Foreign Scan";

    @Test(alwaysRun = true)
    public void runForeignTableTests() {
        
        String query1 = "select * from other_world";
        V2.requireData("select 1 from other_world", TestUtils.createForeignTable("world"));
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
