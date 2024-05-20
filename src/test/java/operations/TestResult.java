package operations;

import operations.testplan.TestPlan;
import operations.utils.TestCLI;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.TestUtils;

import static bench.V2.args;

public class TestResult extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestResult.class);
    private static final String expectedPlanType = "Result";

    @Test
    public void runFunctionTests() {
        
        String query1 = "select 1";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
