package operations;

import bench.V2;
import com.google.gson.JsonObject;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static operations.utils.JsonOperations.explainResultsJson;

public class TestGroupAggregate extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestGroupAggregate.class);
    private static final String expectedPlanType = "Aggregate";

    public static void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            String actualPlanType = resultsJson.getAsJsonObject("Plan").get("Node Type").getAsString();
            Boolean hasGroupKey = resultsJson.getAsJsonObject("Plan").has("Group Key");
            try {
                TestUtils.printQueryInfoInFile(logger, query);
                Assert.assertEquals(expectedPlanType, actualPlanType);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: {}", query);
                Assert.assertEquals(true, hasGroupKey);
                logger.info("Plan check completed for GroupAggregate plan in query: {}", query);
                TestUtils.checkTime(logger, resultsJson);
                TestUtils.testQuery(logger, query);
            } catch (AssertionError e) {
                TestUtils.openWriter();
                TestUtils.writer.println();
                TestUtils.writer.close();
                logger.error("{} in query: {}", e, query);
                throw new RuntimeException(e);
            }
        }
    }

    @Test(alwaysRun = true, priority = 4)
    public void runLargeTablesTests() {
        String query1 = "select x, count(*) from large_table group by x order by x";
        V2.requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        sql("analyze large_table");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

}
