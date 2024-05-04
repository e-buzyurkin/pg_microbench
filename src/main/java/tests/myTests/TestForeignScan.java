package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.args;
import static bench.V2.requireData;

public class TestForeignScan {
    private static final Logger logger = LoggerFactory.getLogger(TestFunctionScan.class);
    private static final String expectedPlanType = "Function Scan";

    @Test
    public void runForeignTableTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from other_world";
        requireData("select 1 from other_world", "myTests/ForeignTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
