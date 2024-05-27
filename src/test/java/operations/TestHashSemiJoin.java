package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.requireData;

public class TestHashSemiJoin extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestHashSemiJoin.class);
    private static final String expectedPlanType = "Hash Join";
    private static final String planElementName = "Join Type";
    private static final String expectedPlanElement = "Semi";


    @Test(alwaysRun = true)
    public void runSmallLargeTablesTests() {

        String query1 = "select * from small_table where exists (select * from " +
                "large_table where small_table.x = large_table.x)";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runMediumLargeTablesTests() {
        
        String query1 = "select * from medium_table where exists (select * from " +
                "large_table where medium_table.x = large_table.x)";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true, priority = 4)
    public void runLargeTablesTests() {

        String query1 = "select * from large_table where exists (select * from " +
                "huge_table where large_table.x = huge_table.x)";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
