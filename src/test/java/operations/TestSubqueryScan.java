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

public class TestSubqueryScan extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestSubqueryScan.class);
    private static final String expectedPlanType = "Subquery Scan";

    public static void testQueries(String[] queries) {
        for (String query : queries) {
            explain(logger, query);
            JsonObject resultsJson = explainResultsJson(query);
            JsonPlan jsonPlan = findPlanElement(resultsJson, "Node Type", expectedPlanType);
            String actualPlanElement = jsonPlan.getPlanElement();
            try {
                Assert.assertEquals(expectedPlanType, actualPlanElement);
                logger.info("Plan check completed for " + expectedPlanType + " plan in query: {}", query);
                TestUtils.checkTime(logger, resultsJson);
                TestUtils.testQuery(logger, query);
            } catch (AssertionError e) {
                logger.error("{} in query: {}", e, query);
                throw new RuntimeException(e);
            }
        }
    }

    @Test(alwaysRun = true, priority = 2)
    public void runSmallTablesTests() {

        String query1 = "select * from small_table except select * from small_table";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test(alwaysRun = true, priority = 3)
    public void runMediumTablesTests() {

        String query1 = "select * from medium_table except select * from medium_table";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }

    @Test(alwaysRun = true, priority = 4)
    public void runLargeTablesTests() {

        String query1 = "select * from large_table except select * from large_table";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        testQueries(queries);
    }
}

    
