package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestCLI;
import operations.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestGather extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestGather.class);
    private static final String expectedPlanType = "Gather";

    @Test
    public void runHugeTablesTests() {
        
        String query1 = "select sum(x) from huge_table union all select count(*) from huge_table";
        V2.requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
