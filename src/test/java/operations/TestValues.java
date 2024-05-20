package operations;

import operations.testplan.TestPlan;
import operations.utils.TestCLI;
import operations.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestValues extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestValues.class);
    private static final String expectedPlanType = "Values Scan";

    @Test
    public void runFunctionTests() {
        
        String query1 = "values (1), (2), (3)";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
