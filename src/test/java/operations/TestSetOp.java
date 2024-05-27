package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSetOp extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestSetOp.class);
    private static final String expectedPlanType = "SetOp";

    @Test(alwaysRun = true)
    public void runSmallTableTests() {
        
        String query1 = "select * from small_table intersect select * from small_table";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true, priority = 3)
    public void runMediumTablesTests() {
        
        String query1 = "select * from medium_table intersect select * from medium_table";
        V2.requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true, priority = 4)
    public void runLargeTablesTests() {
        
        String query1 = "select * from large_table intersect select * from large_table";
        V2.requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

//    @Test(alwaysRun = true)
//    public void runHugeTablesTests() {
//
//        String query1 = "select * from huge_table intersect select * from huge_table";
//        V2.requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
//        String[] queries = new String[]{query1};
//        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
//    }

    
}
