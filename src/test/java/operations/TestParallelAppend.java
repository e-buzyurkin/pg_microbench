package operations;

import operations.testplan.TestPlan;
import operations.utils.TestCLI;
import org.junit.Before;
import org.junit.Test;
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

    @Test
    public void runHugeTablesTests() {
        
        String query1 = "select * from huge_table union all select * from huge_table " +
                "order by 1";
        //You can remove 'requireData' if checking query takes too much time and you sure you've created
        // all needed tables.
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
