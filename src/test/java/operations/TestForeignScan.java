package operations;

import bench.V2;
import operations.utils.TestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestForeignScan {
    private static final Logger logger = LoggerFactory.getLogger(TestForeignScan.class);
    private static final String expectedPlanType = "Foreign Scan";

    @Test
    public void runForeignTableTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from other_world";
        V2.requireData("select 1 from other_world", "tests/operations/ForeignTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
