package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestIncrementalSort extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestIncrementalSort.class);

    private static final String expectedPlanType = "Incremental Sort";

    @Test(alwaysRun = true)
    public void runLargeTablesTests() {
        
        String query1 = "select * from large_child_table order by id, parent_id";
        V2.requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

//    @Test(alwaysRun = true)
//    public void runHugeTablesTests() {
//
//        String query1 = "select * from huge_child_table order by id, parent_id";
//        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
//        String[] queries = new String[]{query1};
//        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
//    }

    
}
