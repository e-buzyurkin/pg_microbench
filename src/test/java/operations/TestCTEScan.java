package operations;

import operations.testplan.TestPlan;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.TestUtils;

import static bench.V2.requireData;

public class TestCTEScan extends TestPlan  {
    private static final Logger logger = LoggerFactory.getLogger(TestCTEScan.class);
    private static final String expectedPlanType = "CTE Scan";

    @Test(alwaysRun = true)
    public void runFunctionsTests() {
        String query1 = "with source as materialized (select 1) select * from source";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
