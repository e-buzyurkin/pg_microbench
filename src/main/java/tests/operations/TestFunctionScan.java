package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestFunctionScan {

    private static final Logger logger = LoggerFactory.getLogger(TestFunctionScan.class);
    private static final String expectedPlanType = "Function Scan";

    @Test
    public void runFunctionTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from generate_series(1, 10)";
        String query2 = "select * from generate_series(1, 1000)";
        String query3 = "select * from generate_series(1, 100000)";
        String[] queries = new String[]{query1, query2, query3};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
