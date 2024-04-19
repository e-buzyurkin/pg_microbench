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

public class TestParallelHashJoin {

    private static final Logger logger = LoggerFactory.getLogger(TestParallelHashJoin.class);

    private void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = TestUtils.findPlanElement(resultsJson, "Hash Join");
            String actualPlanType = jsonPlan.getPlan();
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
        //1:N
        String query1 = "select count(*) from huge_parent_table inner join huge_child_table on" +
                " huge_parent_table.id = huge_child_table.parent_id";
        //1:1
        String query2 = "select count(*) from huge_profile_table inner join huge_users_table on" +
                " huge_profile_table.id = huge_users_table.id";
        //N:N
        String query3 = "select count(*) from huge_business_table sb inner join " +
                "huge_first_partner_table fb on sb.first_partner = fb.id inner join " +
                "huge_second_partner_table sp ON sb.second_partner = sp.id";
        requireData(RequiredData.checkTables("huge"), "myTests/HugeTables.sql");
        String[] queries = new String[]{query1, query2, query3};
        testQueries(queries);
    }
}
