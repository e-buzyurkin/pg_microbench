package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestMergeAppend extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestMergeAppend.class);
    private static final String expectedPlanType = "Merge Append";

    @Test(alwaysRun = true)
    public void runFunctionTests() {
        
        String query1 = "(values (1), (2) order by 1) union all (values(3), (4) order by 1) order by 1";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true)
    public void runSmallTablesTests() {
        
        String query1 = "(select * from small_table order by 1) union all (select * from small_table order by 1) order by 1";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true)
    public void runMediumTablesTests() {
        
        String query1 = "(select * from medium_table order by 1) union all (select * from medium_table order by 1) order by 1";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true)
    public void runLargeTablesTests() {
        
        String query1 = "(select * from large_table order by 1) union all (select * from large_table order by 1) order by 1";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
