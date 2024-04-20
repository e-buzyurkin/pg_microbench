package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class TestMaterialize {
    private static final Logger logger = LoggerFactory.getLogger(TestMaterialize.class);
    private static final String expectedPlanType = "Materialize";
    //TODO incorrect query, it takes too long time

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from large_table s1, large_table s2 where s1.x != s2.x";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueries(logger, queries, expectedPlanType);
    }
}
