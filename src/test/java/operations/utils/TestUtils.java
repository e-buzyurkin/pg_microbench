package operations.utils;

import bench.v2.Results;
import com.google.gson.JsonObject;

import operations.bpftrace.BpfTraceRunner;
import org.slf4j.Logger;
import org.testng.Assert;

import java.io.*;


import static bench.V2.*;
import static operations.utils.JsonOperations.explainResultsJson;
import static operations.utils.JsonOperations.findPlanElement;

public class TestUtils {

    private static final String fileName = "results.txt";
    private static BpfTraceRunner runner;
    public static PrintWriter writer;


    public static void openWriter() {
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startProfiling() {
        if (params.isProfiling) {
            runner = new BpfTraceRunner();
            runner.startBpfTrace();
        }
    }

    private static void finishProfiling(Logger logger) {
        if (params.isProfiling) {
            runner.stopBpfTrace();
            QueryProfiler profiler = new QueryProfiler();
            profiler.profile(logger);
        }
    }

    public static void testQuery(Logger logger, String query, Object... binds) {
        startProfiling();
        Results parallelState = parallel((state) -> sql(query, binds));
        finishProfiling(logger);
        if (parallelState != null) {
            openWriter();
            writer.print(parallelState.iterations + " ");
            writer.print(parallelState.tps + " ");
            writer.println(parallelState.tpsLast5sec);
            writer.close();
        }
    }

    public static void checkTime(Logger logger, JsonObject explainResults) {
        String executionTime = explainResults.get("Execution Time").getAsString();
        logger.info("Sql query completed after {} ms", executionTime);
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

    public static void testQueriesOnPlanElement(Logger logger, String[] queries,
                                                String planElementName, String expectedPlanElement) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanElement = findPlanElement(resultsJson, planElementName, expectedPlanElement).
                    getJson().get(planElementName).getAsString();
            assertPlanElements(logger, query, planElementName, expectedPlanElement, actualPlanElement);
            checkTime(logger, resultsJson);
            TestUtils.testQuery(logger, query);
        }
    }

    public static void testQueriesOnPlanAndPlanElement(Logger logger, String[] queries, String expectedPlanType,
                                                       String planElementName, String expectedPlanElement) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = findPlanElement(resultsJson, "Node Type", expectedPlanType);
            String actualPlanType = jsonPlan.getPlanElement();
            String actualPlanElement = "";
            if (jsonPlan.getJson().has(planElementName)) {
                actualPlanElement = jsonPlan.getJson().get(planElementName).getAsString();
            }
            assertPlans(logger, query, expectedPlanType, actualPlanType);
            assertPlanElements(logger, query, planElementName, expectedPlanElement, actualPlanElement);
            TestUtils.testQuery(logger, query);
        }
    }

    private static void assertPlanElements(Logger logger, String query, String planElementName,
                                           String expectedPlanElement, String actualPlanElement) {
        try {
            printQueryInfoInFile(logger, query);
            Assert.assertEquals(expectedPlanElement, actualPlanElement);
            logger.info("{} check completed for {} {} in query: {}", planElementName,
                    expectedPlanElement, planElementName, query);
        } catch (AssertionError e) {
            logger.error("{} in query: {}", e, query);
            openWriter();
            writer.println();
            writer.close();
            throw new RuntimeException(e);
        }
    }

    private static void assertPlans(Logger logger, String query, String expectedPlanType, String actualPlanType) {
        try {
            printQueryInfoInFile(logger, query);
            Assert.assertEquals(expectedPlanType, actualPlanType);
            logger.info("Plan element check completed for {} plan in query: {}", expectedPlanType, query);
        } catch (AssertionError e) {
            logger.error("{} in query: {}", e, query);
            openWriter();
            writer.println();
            writer.close();
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

    public static void printQueryInfoInFile(Logger logger, String query) {
        openWriter();
        writer.print(logger.getName().replace("Test", "").replace("operations.", "") + "; ");
        writer.print(query + "; ");
        writer.close();
    }

}
