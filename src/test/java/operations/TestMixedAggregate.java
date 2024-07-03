package operations;

import bench.V2;
import com.google.gson.JsonObject;
import operations.testplan.TestPlan;
import operations.utils.JsonPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static operations.utils.JsonOperations.explainResultsJson;
import static operations.utils.JsonOperations.findPlanElement;

public class TestMixedAggregate extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestMixedAggregate.class);
    private static final String expectedPlanType = "Aggregate";
    private static final String planElementName = "Strategy";
    private static final String expectedPlanElementName = "Mixed";

    public static void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = findPlanElement(resultsJson, planElementName, expectedPlanElementName);
            String actualStrategy = jsonPlan.getPlanElement();
            String actualPlanType = jsonPlan.getJson().get("Node Type").getAsString();
            try {
                TestUtils.printQueryInfoInFile(logger, query);
                Assert.assertEquals(expectedPlanType, actualPlanType);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: " + query);
                Assert.assertEquals(expectedPlanElementName, actualStrategy);
                logger.info("Plan check completed for " + expectedPlanElementName + " plan strategy in query: " + query);
                TestUtils.checkTime(logger, resultsJson);
                TestUtils.testQuery(logger, query);
            } catch (AssertionError e) {
                logger.error(e + " in query: " + query);
                TestUtils.openWriter();
                TestUtils.writer.println();
                TestUtils.writer.close();
                throw new RuntimeException(e);
            }
        }
    }

    @Test(alwaysRun = true, priority = 2)
    public void runSmallTablesTests() {
        
        String query1 = "select distinct x from small_table group by rollup(x)";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test(alwaysRun = true, priority = 3)
    public void runMediumTablesTests() {
        
        String query1 = "select distinct x from medium_table group by rollup(x)";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

//    @Test(alwaysRun = true)
//    public void runLargeTablesTests() {
//        String[] args = System.getProperty("args").split("\\s+");
//        args(args);
//        String query1 = "select distinct x from large_table group by rollup(x)";
//        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
//        String[] queries = new String[]{query1};
//        testQueries(queries);
//    }

    
}
