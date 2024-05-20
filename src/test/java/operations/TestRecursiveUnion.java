package operations;

import operations.testplan.TestPlan;
import operations.utils.TestCLI;
import operations.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.args;

public class TestRecursiveUnion extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestRecursiveUnion.class);
    private static final String expectedPlanType = "Recursive Union";

    @Test
    public void runFunctionsTests() {
        
        String query1 = "with recursive source (counter) as (" +
                "select 1 union all select counter + 1 from source where counter < 10) " +
                "select * from source";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnSubPlan(logger, queries, expectedPlanType);
    }

    
}
