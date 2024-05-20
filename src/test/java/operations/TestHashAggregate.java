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

import static bench.V2.*;

public class TestHashAggregate extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestHashAggregate.class);

    private static final String expectedPlanType = "Aggregate";
    private static final String planElementName = "Strategy";
    private static final String expectedPlanElement = "Hashed";

    @Test
    public void runSmallTablesTests() {
        
        String query1 = "select distinct x from small_table";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runMediumTablesTests() {
        
        String query1 = "select distinct x from medium_table";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runLargeTablesTests() {
        
        String query1 = "select distinct x from large_table";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
