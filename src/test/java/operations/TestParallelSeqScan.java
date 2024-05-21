package operations;


import operations.testplan.TestPlan;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import static bench.V2.*;

public class TestParallelSeqScan extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestParallelSeqScan.class);
    private static final String expectedPlanType = "Seq Scan";
    private static final String planElementName = "Parallel Aware";
    private static final String expectedPlanElement = "true";

    @Test(alwaysRun = true)
    public void runHugeTablesTests() {
        
        String query1 = "select sum(x) from huge_table";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
