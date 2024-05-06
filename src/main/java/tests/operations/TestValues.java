package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestValues {
    private static final Logger logger = LoggerFactory.getLogger(TestValues.class);
    private static final String expectedPlanType = "Values Scan";

    @Test
    public void runFunctionTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "values (1), (2), (3)";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
