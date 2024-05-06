package tests.operations;

import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.*;
import static tests.operations.utils.TestUtils.checkTime;
import static tests.operations.utils.TestUtils.explainResultsJson;

public class TestHashAntiJoin {
    private static final Logger logger = LoggerFactory.getLogger(TestHashAntiJoin.class);

    private void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanType = resultsJson.getAsJsonObject("Plan").
                    get("Node Type").getAsString();
            String actualJoinType = resultsJson.getAsJsonObject("Plan").
                    get("Join Type").getAsString();
            try {
                String expectedPlanType = "Hash Join";
                Assertions.assertEquals(expectedPlanType, actualPlanType);
                String expectedJoinType = "Anti";
                Assertions.assertEquals(expectedJoinType, actualJoinType);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: " + query);
                logger.info("Plan check completed for " + expectedJoinType + " join type in query: " + query);
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
        String query1 = "select * from small_table_1 where not exists (select * from " +
                "small_table_2 where small_table_2.x = small_table_1.x)";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_table_1 where not exists (select * from " +
                "medium_table_2 where medium_table_2.x = medium_table_1.x)";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from large_table_1 where not exists (select * from " +
                "large_table_2 where large_table_2.x = large_table_1.x)";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }
}
