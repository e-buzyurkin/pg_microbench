package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.TestUtils;

import static bench.V2.*;


public class TestWorkTableScan {
    private static final Logger logger = LoggerFactory.getLogger(TestWorkTableScan.class);
    private static final String expectedPlanType = "WorkTable Scan";

    @Test
    public void runFunctionsTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "with recursive source (counter) as (" +
                "select 1 union all select counter + 1 from source where counter < 10) " +
                "select * from source";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnSubPlan(logger, queries, expectedPlanType);
    }
}
