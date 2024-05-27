package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestGroup extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestGroup.class);
    private static final String expectedPlanType = "Group";


    @Test(alwaysRun = true, priority = 3)
    public void runMediumTablesTests() {
        
        String query1 = "select x from medium_table where x < 0 group by x";
        V2.requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        sql("analyze medium_table");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true, priority = 4)
    public void runLargeTablesTests() {
        
        String query1 = "select x from large_table where x < 0 group by x";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        sql("analyze large_table");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
