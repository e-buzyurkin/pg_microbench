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
import static tests.myTests.testUtils.TestUtils.explainResultsJson;

public class TestParallelSeqScan {
    private static final Logger logger = LoggerFactory.getLogger(TestParallelSeqScan.class);
    private static final String expectedPlanType = "TestParallelSeqScan";

    //TODO Fix all

    private void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = TestUtils.findPlanElement(resultsJson, "Node Type", "Hash Join");
            String actualPlanType = jsonPlan.getPlanElement();
            Boolean isParallel = jsonPlan.getJson().get("Parallel Aware").getAsBoolean();
            try {
                String expectedPlanType = "Hash Join";
                Assertions.assertEquals(expectedPlanType, actualPlanType);
                Assertions.assertEquals(isParallel, true);
                logger.info("Plan check completed for {} plan in query: {}", expectedPlanType, query);
                checkTime(logger, resultsJson);
                TestUtils.testQuery(logger, query);
            } catch (AssertionError e) {
                logger.error("{} in query: {}", e, query);
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select sum(x) from huge_table";
        requireData(RequiredData.checkTables("huge"), "myTests/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
