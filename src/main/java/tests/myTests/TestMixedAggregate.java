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

public class TestMixedAggregate {
    private static final Logger logger = LoggerFactory.getLogger(TestMixedAggregate.class);
    private static final String expectedPlanType = "Aggregate";
    //TODO find new queries

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
        String query1 = "select distinct x from small_table group by rollup(x)";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select distinct x from medium_table group by rollup(x)";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select distinct x from large_table group by rollup(x)";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }
}
