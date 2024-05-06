package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestParallelAppend {
    private static final Logger logger = LoggerFactory.getLogger(TestParallelAppend.class);
    private static final String expectedPlanType = "Append";
    private static final String planElementName = "Parallel Aware";
    private static final String expectedPlanElement = "true";

    @Test
    public void runSuperHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from huge_table union all select * from huge_table " +
                "order by 1";
        //You can remove 'requireData' if checking query takes too much time and you sure you've created
        // all needed tables.
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }
}
