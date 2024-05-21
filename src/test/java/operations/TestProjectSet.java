package operations;

import operations.testplan.TestPlan;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestProjectSet extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestProjectSet.class);
    private static final String expectedPlanType = "ProjectSet";

    @Test(alwaysRun = true)
    public void runFunctionsTests() {
        
        String query1 = "select generate_series(1, 10)";
        String query2 = "select generate_series(1, 1000)";
        String query3 = "select generate_series(1, 100000)";
        String[] queries = new String[]{query1, query2, query3};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
