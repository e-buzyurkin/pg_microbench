package operations;

import operations.testplan.TestPlan;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestValues extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestValues.class);
    private static final String expectedPlanType = "Values Scan";

    @Test(alwaysRun = true)
    public void runFunctionTests() {
        
        String query1 = "values (1), (2), (3)";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
