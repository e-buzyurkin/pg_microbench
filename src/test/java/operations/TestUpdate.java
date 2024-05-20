package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestCLI;
import operations.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.args;
import static bench.V2.requireData;

public class TestUpdate extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestUpdate.class);
    private static final String expectedPlanType = "ModifyTable";
    private static final String planElementName = "Operation";
    private static final String expectedPlanElement = "Update";

    @Test
    public void runSmallTablesTests() {
        
        String query1 = "update small_table set x = 1 where x = 1";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runMediumTablesTests() {
        
        String query1 = "update medium_table set x = 1 where x = 1";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runLargeTablesTests() {
        
        String query1 = "update large_table set x = 1 where x = 1";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runHugeTablesTests() {
        
        String query1 = "update huge_table set x = 1 where x = 1";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
