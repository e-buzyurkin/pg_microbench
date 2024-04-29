package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class TestAppend {
    private static final Logger logger = LoggerFactory.getLogger(TestAppend.class);
    private static final String expectedPlanType = "Append";

    @Test
    public void runFunctionTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select 1 union all select 2";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, "Append");
    }

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from small_table_1 union all select * from small_table_2";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_table_1 union all select * from medium_table_2";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from large_table_1 union all select * from large_table_2";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
