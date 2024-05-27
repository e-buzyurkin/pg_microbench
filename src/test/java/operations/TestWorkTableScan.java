package operations;

import operations.testplan.TestPlan;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.TestUtils;


public class TestWorkTableScan extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestWorkTableScan.class);
    private static final String expectedPlanType = "WorkTable Scan";

    @Test(alwaysRun = true)
    public void runFunctionTests() {
        
        String query1 = "with recursive source (counter) as (" +
                "select 1 union all select counter + 1 from source where counter < 10) " +
                "select * from source";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnSubPlan(logger, queries, expectedPlanType);
    }

    
}
