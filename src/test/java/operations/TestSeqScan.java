package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.requireData;

public class TestSeqScan extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestSeqScan.class);
    private static final String expectedPlanType = "Seq Scan";

    @Test(alwaysRun = true, priority = 2)
    public void runSmallTablesTests() {
        
        String query1 = "select * from small_table";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
