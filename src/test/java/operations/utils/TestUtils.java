package operations.utils;

import bench.v2.Results;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.jupiter.api.Assertions;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;

import java.io.*;
import java.util.List;

import static bench.V2.*;

public class TestUtils {

    private static final String fileName = "results.txt";
    public static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void testQuery(String query, Object... binds) {

        Results parallelState = parallel((state) -> sql(query, binds));


        if (parallelState != null) {

            //writer.print(query + "; ");
            writer.print(parallelState.iterations + " ");
            writer.print(parallelState.tps + " ");
            writer.println(parallelState.tpsLast5sec);
            writer.close();
        }
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
            for (JsonElement jsonElement : jsonArray) {
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
            TestUtils.testQuery(query);
        }
    }

    public static void testQueriesOnSubPlan(Logger logger, String[] queries, String expectedPlanType) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanType = findPlanElement(resultsJson, "Node Type", expectedPlanType).getPlanElement();
            assertPlans(logger, query, expectedPlanType, actualPlanType);
            checkTime(logger, resultsJson);
            TestUtils.testQuery(query);
        }
    }

    public static void testQueriesOnPlanElement(Logger logger, String[] queries,
                                                String planElementName, String expectedPlanElement) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanElement = TestUtils.findPlanElement(resultsJson, planElementName, expectedPlanElement).
                    getJson().get(planElementName).getAsString();
            assertPlanElements(logger, query, planElementName, expectedPlanElement, actualPlanElement);
            checkTime(logger, resultsJson);
            TestUtils.testQuery(query);
        }
    }

    public static void testQueriesOnPlanAndPlanElement(Logger logger, String[] queries, String expectedPlanType,
                                                       String planElementName, String expectedPlanElement) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = TestUtils.findPlanElement(resultsJson, "Node Type", expectedPlanType);
            String actualPlanType = jsonPlan.getPlanElement();
            String actualPlanElement = "";
            if (jsonPlan.getJson().has(planElementName)) {
                actualPlanElement = jsonPlan.getJson().get(planElementName).getAsString();
            }
            assertPlans(logger, query, expectedPlanType, actualPlanType);
            assertPlanElements(logger, query, planElementName, expectedPlanElement, actualPlanElement);
            TestUtils.testQuery(query);
        }
    }

    private static void assertPlanElements(Logger logger, String query, String planElementName,
                                           String expectedPlanElement, String actualPlanElement) {
        try {
            Assertions.assertEquals(expectedPlanElement, actualPlanElement);
            logger.info("{} check completed for {} {} in query: {}", planElementName,
                    expectedPlanElement, planElementName, query);
        } catch (AssertionError e) {
            logger.error("{} in query: {}", e, query);
            writer.println();
            throw new RuntimeException(e);
        }
    }

    private static void assertPlans(Logger logger, String query, String expectedPlanType, String actualPlanType) {
        try {
            Assertions.assertEquals(expectedPlanType, actualPlanType);
            logger.info("Plan element check completed for {} plan in query: {}", expectedPlanType, query);
        } catch (AssertionError e) {
            logger.error("{} in query: {}", e, query);
            writer.println();
            throw new RuntimeException(e);
        }
    }

    public static String createForeignTable(String tableName) {
        String foreignTableName = "other_" + tableName;
        sql("create table if not exists world (\n" +
                "    greeting TEXT\n" +
                ")");
        sql("create extension if not exists postgres_fdw");
        sql("create server if not exists postgres_fdw_test\n" +
                "foreign data wrapper postgres_fdw\n" +
                "options (host '" + db.host + "', port '" + db.port + "', dbname '" + db.dbName + "')");
        sql("create user mapping if not exists for public server postgres_fdw_test\n" +
                "options (password '')");
        sql("create foreign table if not exists " + foreignTableName + " (greeting TEXT)\n" +
                "server postgres_fdw_test\n" +
                "options (table_name '" + tableName + "')");
        return foreignTableName;
    }

}
