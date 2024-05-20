package operations;

import operations.testplan.TestPlan;
import operations.utils.TestCLI;
import operations.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.args;

public class TestProjectSet extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestProjectSet.class);
    private static final String expectedPlanType = "ProjectSet";

    @Test
    public void runFunctionsTests() {
        
        String query1 = "select generate_series(1, 10)";
        String query2 = "select generate_series(1, 1000)";
        String query3 = "select generate_series(1, 100000)";
        String query4 = "select generate_series(1, 10000000)";
        String[] queries = new String[]{query1, query2, query3, query4};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
