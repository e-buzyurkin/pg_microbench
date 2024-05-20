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

public class TestDelete extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestDelete.class);
    private static final String expectedPlanType = "ModifyTable";
    private static final String planElementName = "Operation";
    private static final String expectedPlanElement = "Delete";

    @Test
    public void runSmallTablesTests() {
        String query1 = "delete from small_table where false";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runMediumTablesTests() {
        String query1 = "delete from medium_table where false";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runLargeTablesTests() {
        String query1 = "delete from large_table where false";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runHugeTablesTests() {
        String query1 = "delete from huge_table where false";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
