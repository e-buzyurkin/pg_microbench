package tests.myTests;

import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.JsonPlan;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;
import static tests.myTests.testUtils.TestUtils.checkTime;

public class TestSubPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestSubPlan.class);
    private static final String expectedPlanType = "SubPlan";

    public static void testQueries(Logger logger, String[] queries, String expectedPlanType) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = TestUtils.explainResultsJson(query);
            JsonPlan jsonPlan = TestUtils.findPlanElement(resultsJson, "Parent Relationship", "SubPlan");
            String actualPlanElement = jsonPlan.getPlanElement();
            try {
                Assertions.assertEquals(expectedPlanType, actualPlanElement);
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
        String query1 = "select * from small_table where small_table.x not in (select small_table.x from small_table)";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        String[] queries = new String[]{query1};
        testQueries(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_table where medium_table.x not in (select medium_table.x from medium_table)";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        testQueries(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from large_table where large_table.x not in (select large_table.x from large_table)";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        testQueries(logger, queries, expectedPlanType);
    }
}
