package operations;

import operations.testplan.TestPlan;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.TestUtils;

public class TestResult extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestResult.class);
    private static final String expectedPlanType = "Result";

    @Test(alwaysRun = true)
    public void runFunctionTests() {
        
        String query1 = "select 1";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
