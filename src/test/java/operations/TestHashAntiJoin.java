package operations;

import operations.testplan.TestPlan;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import static bench.V2.*;

public class TestHashAntiJoin extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestHashAntiJoin.class);
    private static final String expectedPlanType = "Hash Join";
    private static final String planElementName = "Join Type";
    private static final String expectedPlanElement = "Anti";

    @Test(alwaysRun = true)
    public void runSmallTablesTests() {
        
        String query1 = "select * from small_table_1 where not exists (select * from " +
                "small_table_2 where small_table_2.x = small_table_1.x)";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runMediumTablesTests() {
        
        String query1 = "select * from medium_table_1 where not exists (select * from " +
                "medium_table_2 where medium_table_2.x = medium_table_1.x)";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runLargeTablesTests() {
        
        String query1 = "select * from large_table_1 where not exists (select * from " +
                "large_table_2 where large_table_2.x = large_table_1.x)";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
