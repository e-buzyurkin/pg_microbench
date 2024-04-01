package tests.myTests;

import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;
import static tests.myTests.testUtils.TestUtils.checkTime;
import static tests.myTests.testUtils.TestUtils.explainResultsJson;

public class TestHashJoin {

    private static final Logger logger = LoggerFactory.getLogger(TestHashJoin.class);

    private void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanType = resultsJson.getAsJsonObject("Plan").
                    get("Node Type").getAsString();
            try {
                String expectedPlanType = "Hash Join";
                Assertions.assertEquals(expectedPlanType, actualPlanType);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: " + query);
                checkTime(logger, resultsJson);
                TestUtils.testQuery(logger, query);
            } catch (AssertionError e) {
                logger.error(e + " in query: " + query);
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from small_table_1 where exists (select * from " +
                "small_table_2 where small_table_2.x = small_table_1.x)";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_table_1 where exists (select * from " +
                "medium_table_2 where medium_table_2.x = medium_table_1.x)";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from large_table_1 where exists (select * from " +
                "large_table_2 where large_table_2.x = large_table_1.x)";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    //TODO add some examples with tables. (1, 1), (1, N) , (N, N)
}
