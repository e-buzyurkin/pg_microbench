package tests.operations;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestParallelSeqScan {
    private static final Logger logger = LoggerFactory.getLogger(TestParallelSeqScan.class);
    private static final String expectedPlanType = "Seq Scan";
    private static final String planElementName = "Parallel Aware";
    private static final String expectedPlanElement = "true";

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select sum(x) from huge_table";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }
}
