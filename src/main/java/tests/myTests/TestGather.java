package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class TestGather {
    private static final Logger logger = LoggerFactory.getLogger(TestGather.class);

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select count(*) from huge_table";
        String query2 = "select sum(col_1) from huge_table union all select count(*) from huge_table";
        requireData(RequiredData.checkTables("huge"), "myTests/HugeTables.sql");

        TestUtils.testQueries(logger, TestAggregate.class.getSimpleName().replace("Test", ""),
                new String[]{query1, query2});
    }
}
