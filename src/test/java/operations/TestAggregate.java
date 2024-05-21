package operations;
import operations.testplan.TestPlan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.RequiredData;
import operations.utils.TestUtils;
import org.testng.annotations.Test;

import static bench.V2.*;


public class TestAggregate extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestAggregate.class);
    private static final String expectedPlanType = "Aggregate";

    @Test(alwaysRun = true)
    public void runSmallTablesTests() {
        String query1 = "select count(*) from small_table";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true)
    public void runMediumTablesTests() {
        String query1 = "select count(*) from medium_table";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test(alwaysRun = true)
    public void runLargeTablesTests() {
        String query1 = "select count(*) from large_table";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
