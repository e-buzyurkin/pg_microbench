package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.DropTables;
import operations.utils.RequiredData;
import operations.utils.TestCLI;
import operations.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestIncrementalSort extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestIncrementalSort.class);

    private static final String expectedPlanType = "Incremental Sort";

    @Test
    public void runLargeTablesTests() {
        
        String query1 = "select * from large_child_table order by id, parent_id";
        V2.requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runHugeTablesTests() {
        
        String query1 = "select * from huge_child_table order by id, parent_id";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
