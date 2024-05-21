package operations;

import operations.testplan.TestPlan;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFunctionScan extends TestPlan {

    private static final Logger logger = LoggerFactory.getLogger(TestFunctionScan.class);
    private static final String expectedPlanType = "Function Scan";

    @Test(alwaysRun = true)
    public void runFunctionTests() {
        
        String query1 = "select * from generate_series(1, 10)";
        String query2 = "select * from generate_series(1, 1000)";
        String query3 = "select * from generate_series(1, 100000)";
        String[] queries = new String[]{query1, query2, query3};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
