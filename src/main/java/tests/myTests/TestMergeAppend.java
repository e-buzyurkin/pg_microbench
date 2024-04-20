package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class TestMergeAppend {
    private static final Logger logger = LoggerFactory.getLogger(TestMergeAppend.class);
    private static final String expectedPlanType = "Append";

    //TODO find new queries
    @Test
    public void runFunctionTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "(values (1), (2) order by 1) union all (values(3), (4) order by 1) order by 1";
        String[] queries = new String[]{query1};
        TestUtils.testQueries(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select x from medium_table where x > 5 group by x";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueries(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select x from large_table where x > 5 group by x";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueries(logger, queries, expectedPlanType);
    }
}
