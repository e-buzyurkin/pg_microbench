package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestAppend  extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestAppend.class);
    private static final String expectedPlanType = "Append";

    @Test(alwaysRun = true)
    public void runFunctionTests() {
        String query1 = "select 1 union all select 2";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, "Append");
    }

    @Test(alwaysRun = true)
    public void runSmallTablesTests() {
        String query1 = "select * from small_table_1 union all select * from small_table_2";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true)
    public void runMediumTablesTests() {
        String query1 = "select * from medium_table_1 union all select * from medium_table_2";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true)
    public void runLargeTablesTests() {
        String query1 = "select * from large_table_1 union all select * from large_table_2";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

}
