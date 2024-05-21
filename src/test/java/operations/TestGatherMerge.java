package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestGatherMerge extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestGatherMerge.class);
    private static final String expectedPlanType = "Gather Merge";

    @Test(alwaysRun = true)
    public void runHugeTablesTests() {
        
        String query1 = "select * from huge_parent_table order by 2";
        V2.requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
