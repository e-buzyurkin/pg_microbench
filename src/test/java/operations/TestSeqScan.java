package operations;

import bench.V2;
import operations.utils.RequiredData;
import operations.utils.TestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.args;
import static bench.V2.requireData;

public class TestSeqScan {
    private static final Logger logger = LoggerFactory.getLogger(TestSeqScan.class);
    private static final String expectedPlanType = "Seq Scan";

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from small_table";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
