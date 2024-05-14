package operations;

import bench.V2;
import operations.utils.RequiredData;
import operations.utils.TestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestGatherMerge {
    private static final Logger logger = LoggerFactory.getLogger(TestGatherMerge.class);
    private static final String expectedPlanType = "Gather Merge";

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from huge_table order by 1";
        //You can remove 'requireData' if checking query takes too much time and you sure you've created
        // all needed tables.
        V2.requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
