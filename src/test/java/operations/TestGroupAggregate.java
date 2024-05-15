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

public class TestGroupAggregate {
    private static final Logger logger = LoggerFactory.getLogger(TestGroupAggregate.class);
    private static final String expectedPlanType = "Aggregate";

    public static void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = TestUtils.explainResultsJson(query);
            String actualPlanType = resultsJson.getAsJsonObject("Plan").get("Node Type").getAsString();
            Boolean hasGroupKey = resultsJson.getAsJsonObject("Plan").has("Group Key");
            try {
                Assertions.assertEquals(expectedPlanType, actualPlanType);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: {}", query);
                Assertions.assertEquals(true, hasGroupKey);
                logger.info("Plan check completed for GroupAggregate plan in query: {}", query);
                TestUtils.checkTime(logger, resultsJson);
                TestUtils.testQuery(query);
            } catch (AssertionError e) {
                logger.error("{} in query: {}", e, query);
                throw new RuntimeException(e);
            }
        }
    }


    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select x, count(*) from medium_table group by x order by x";
        V2.requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        sql("analyze medium_table");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select x, count(*) from large_table group by x order by x";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        sql("analyze large_table");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }
}
