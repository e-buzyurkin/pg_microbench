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

public class TestParallelAppend {
    private static final Logger logger = LoggerFactory.getLogger(TestParallelSeqScan.class);
    private static final String expectedPlanType = "Append";

    //TODO needs test
    private void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = TestUtils.findPlanElement(resultsJson, "Node Type", expectedPlanType);
            String actualPlanType = jsonPlan.getPlanElement();
            Boolean isParallel = jsonPlan.getJson().get("Parallel Aware").getAsBoolean();
            try {
                Assertions.assertEquals(expectedPlanType, actualPlanType);
                Assertions.assertEquals(true, isParallel);
                logger.info("Plan check completed for {} plan in query: Parallel {}", expectedPlanType, query);
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
        String query1 = "select * from huge_table union select * from huge_table union " +
                "select * from huge_table order by 1";
        requireData(RequiredData.checkTables("huge"), "myTests/HugeTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }
}
