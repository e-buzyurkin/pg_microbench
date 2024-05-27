package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestSubPlan extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestSubPlan.class);
    private static final String planElementName = "Parent Relationship";
    private static final String expectedPlanElement = "SubPlan";

    @Test(alwaysRun = true, priority = 2)
    public void runSmallTablesTests() {
        
        String query1 = "select * from small_table where small_table.x not in (select small_table.x from small_table)";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true, priority = 3)
    public void runMediumTablesTests() {
        
        String query1 = "select * from medium_table where medium_table.x not in (select medium_table.x from medium_table)";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, planElementName, expectedPlanElement);
    }

    

}
