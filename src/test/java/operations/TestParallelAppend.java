package operations;

import operations.testplan.TestPlan;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import static bench.V2.*;

public class TestParallelAppend extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestParallelAppend.class);
    private static final String expectedPlanType = "Append";
    private static final String planElementName = "Parallel Aware";
    private static final String expectedPlanElement = "true";

    @Test(alwaysRun = true)
    public void runHugeTablesTests() {
        
        String query1 = "select * from huge_table union all select * from huge_table " +
                "order by 1";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
