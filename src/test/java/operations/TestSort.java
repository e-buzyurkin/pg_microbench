package operations;

import operations.testplan.TestPlan;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import static bench.V2.requireData;

public class TestSort extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestSort.class);
    private static final String expectedPlanType = "Sort";

    @Test(alwaysRun = true)
    public void runSmallTablesTests() {
        
        String query1 = "select x, count(*) from small_table group by x order by x";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnSubPlan(logger, queries, expectedPlanType);
    }

    
}
