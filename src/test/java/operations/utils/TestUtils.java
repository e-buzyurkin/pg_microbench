package operations.utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.jupiter.api.Assertions;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;

import java.util.List;

import static bench.V2.*;

public class TestUtils {

    public static void testQuery(Logger logger, String query, Object... binds) {
        parallel((state) -> {
            sql(query);
        });
    }

    public static void checkTime(Logger logger, JsonObject explainResults) {
        String executionTime = explainResults.get("Execution Time").getAsString();
        logger.info("Sql query completed after " + executionTime + " ms");
    }

    public static JsonObject explainResultsJson(String sql, Object... binds) {
        List<PGobject> pGobjectList = select("explain (analyze, verbose, buffers, costs off, format json) " + sql, binds);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(pGobjectList.get(0).getValue(), JsonArray.class);
        return jsonArray.get(0).getAsJsonObject();
    }

    private static JsonPlan findPlanElementRecursive(JsonObject jsonObject, String key, String planElement) {
        JsonPlan result = new JsonPlan("", jsonObject);
        if (jsonObject.has(key)) {
            result = new JsonPlan(jsonObject.get(key).getAsString(), jsonObject);
        }
        if (jsonObject.getAsJsonArray("Plans") != null) {
            String curPlanElement = "";
            if (jsonObject.has(key)) {
                curPlanElement = jsonObject.get(key).getAsString();
            }
            if (curPlanElement.equals(planElement)) {
                return new JsonPlan(curPlanElement, jsonObject);
            }
            JsonArray jsonArray = jsonObject.getAsJsonArray("Plans");
            for (JsonElement jsonElement: jsonArray) {
                result = findPlanElementRecursive(jsonElement.getAsJsonObject(), key, planElement);
                if (result.getPlanElement().equals(planElement)) {
                    return result;
                }
            }
        }
        return result;
    }

    public static JsonPlan findPlanElement(JsonObject jsonObject, String key, String element) {
        return findPlanElementRecursive(jsonObject.getAsJsonObject("Plan"), key, element);
    }

    public static void testQueriesOnMainPlan(Logger logger, String[] queries, String expectedPlanType) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanType = resultsJson.getAsJsonObject("Plan").
                    get("Node Type").getAsString();
            assertPlans(logger, query, expectedPlanType, actualPlanType);
            checkTime(logger, resultsJson);
            TestUtils.testQuery(logger, query);
        }
    }
    
    public static void testQueriesOnSubPlan(Logger logger, String[] queries, String expectedPlanType) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanType = findPlanElement(resultsJson, "Node Type", expectedPlanType).getPlanElement();
            assertPlans(logger, query, expectedPlanType, actualPlanType);
            checkTime(logger, resultsJson);
            TestUtils.testQuery(logger, query);
        }
    }

    public static void testQueriesOnPlanElement(Logger logger, String[] queries, String expectedPlanType,
                                                String planElementName, String expectedPlanElement) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = TestUtils.findPlanElement(resultsJson, "Node Type", expectedPlanType);
            String actualPlanType = jsonPlan.getPlanElement();
            try {
                String actualPlanElement = jsonPlan.getJson().get(planElementName).getAsString();
                Assertions.assertEquals(expectedPlanType, actualPlanType);
                logger.info("Plan check completed for {} plan and in query: {}", expectedPlanType, query);
                Assertions.assertEquals(expectedPlanElement, actualPlanElement);
                logger.info("{} check completed for {} {} in query: {}", planElementName,
                        expectedPlanElement, planElementName, query);
                checkTime(logger, resultsJson);
                TestUtils.testQuery(logger, query);
            } catch (AssertionError e) {
                logger.error("{} in query: {}", e, query);
                throw new RuntimeException(e);
            }
        }
    }

    private static void assertPlans(Logger logger, String query, String expectedPlanType, String actualPlanType) {
        try {
            Assertions.assertEquals(expectedPlanType, actualPlanType);
            logger.info("Plan check completed for " + expectedPlanType + " plan in query: " + query);
        } catch (AssertionError e) {
            logger.error(e + " in query: " + query);
            throw new RuntimeException(e);
        }
    }

    public static String createForeignTable(String databaseName, String tableName) {
        sql("create table " + tableName + " ( "+
                "    greeting TEXT" +
                " )");
        sql("create extension if not exists postgres_fdw");
        sql("create server postgres_fdw_test " +
                "foreign data wrapper postgres_fdw " +
                "options (host 'localhost', dbname '" + databaseName + "')");
        sql("create user mapping for public server postgres_fdw_test " +
                "options (password '')");
        sql("create foreign table other_" + tableName + " (greeting TEXT) " +
                "server postgres_fdw_test " +
                "options (table_name '" + tableName + "')");
        return "other_" + tableName;
    }
}
