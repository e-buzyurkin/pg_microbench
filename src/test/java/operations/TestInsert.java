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

public class TestInsert extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestInsert.class);
    private static final String expectedPlanType = "ModifyTable";
    private static final String planElementName = "Operation";
    private static final String expectedPlanElement = "Insert";

    @Test
    public void runSmallTablesTests() {
        
        String query1 = "INSERT INTO small_table SELECT * FROM generate_series(1,0) WHERE false";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runMediumTablesTests() {
        
        String query1 = "INSERT INTO medium_table SELECT * FROM generate_series(1,0) WHERE false";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runLargeTablesTests() {
        
        String query1 = "INSERT INTO large_table SELECT * FROM generate_series(1,0) WHERE false";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runHugeTablesTests() {
        
        String query1 = "INSERT INTO huge_table SELECT * FROM generate_series(1,0) WHERE false";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
