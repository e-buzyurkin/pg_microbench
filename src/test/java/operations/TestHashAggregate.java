package operations;

import bench.V2;
import com.google.gson.JsonObject;
import operations.utils.RequiredData;
import operations.utils.TestUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestHashAggregate {
    private static final Logger logger = LoggerFactory.getLogger(TestHashAggregate.class);

    private static final String expectedPlanType = "Aggregate";

    public static void testQueries(Logger logger, String[] queries, String expectedPlanType) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = TestUtils.explainResultsJson(query);
            String actualPlanType = resultsJson.getAsJsonObject("Plan").
                    get("Node Type").getAsString();
            String expectedStrategy = "Hashed";
            String actualStrategy = resultsJson.getAsJsonObject("Plan").get("Strategy").getAsString();
            try {
                Assertions.assertEquals(expectedPlanType, actualPlanType);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: " + query);
                Assertions.assertEquals(expectedStrategy, actualStrategy);
                logger.info("Plan strategy check completed for " + expectedStrategy + " plan strategy in query: " + query);
                TestUtils.checkTime(logger, resultsJson);
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
        String query1 = "select distinct x from small_table";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        testQueries(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select distinct x from medium_table";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        testQueries(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select distinct x from large_table";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        testQueries(logger, queries, expectedPlanType);
    }
}
