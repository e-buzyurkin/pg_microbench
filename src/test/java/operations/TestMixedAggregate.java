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

public class TestMixedAggregate extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestMixedAggregate.class);
    private static final String expectedPlanType = "Aggregate";

    public static void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = TestUtils.explainResultsJson(query);
            String expectedStrategy = "Mixed";
            JsonPlan jsonPlan = TestUtils.findPlanElement(resultsJson, "Strategy", expectedStrategy);
            String actualStrategy = jsonPlan.getPlanElement();
            String actualPlanType = jsonPlan.getJson().get("Node Type").getAsString();
            try {
                Assert.assertEquals(expectedPlanType, actualPlanType);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: " + query);
                Assert.assertEquals(expectedStrategy, actualStrategy);
                logger.info("Plan check completed for " + expectedStrategy + " plan strategy in query: " + query);
                TestUtils.checkTime(logger, resultsJson);
                TestUtils.testQuery(query);
            } catch (AssertionError e) {
                logger.error(e + " in query: " + query);
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
