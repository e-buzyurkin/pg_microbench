package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestMergeAppend {
    private static final Logger logger = LoggerFactory.getLogger(TestMergeAppend.class);
    private static final String expectedPlanType = "Merge Append";

    @Test
    public void runFunctionTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "(values (1), (2) order by 1) union all (values(3), (4) order by 1) order by 1";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "(select * from small_table order by 1) union all (select * from small_table order by 1) order by 1";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "(select * from medium_table order by 1) union all (select * from medium_table order by 1) order by 1";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "(select * from large_table order by 1) union all (select * from large_table order by 1) order by 1";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
