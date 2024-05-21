package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.requireData;

public class TestMerge extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestMerge.class);
    private static final String expectedPlanType = "ModifyTable";
    private static final String planElementName = "Operation";
    private static final String expectedPlanElement = "Merge";

    @Test(alwaysRun = true)
    public void runSmallTablesTests() {
        
        String query1 = "MERGE INTO small_parent_table USING (VALUES (1)) m(id)" +
                " ON small_parent_table.id = m.id" +
                " WHEN NOT MATCHED THEN DO NOTHING" +
                " WHEN MATCHED THEN DO NOTHING";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runMediumTablesTests() {
        
        String query1 = "MERGE INTO medium_parent_table USING (VALUES (1)) m(id) " +
                "ON medium_parent_table.id = m.id " +
                "WHEN NOT MATCHED THEN DO NOTHING " +
                "WHEN MATCHED THEN DO NOTHING";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runLargeTablesTests() {
        
        String query1 = "MERGE INTO large_parent_table USING (VALUES (1)) m(id) " +
                "ON large_parent_table.id = m.id " +
                "WHEN NOT MATCHED THEN DO NOTHING " +
                "WHEN MATCHED THEN DO NOTHING ";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runHugeTablesTests() {
        
        String query1 = "MERGE INTO huge_parent_table USING (VALUES (1)) m(id) " +
                "ON huge_parent_table.id = m.id " +
                "WHEN NOT MATCHED THEN DO NOTHING " +
                "WHEN MATCHED THEN DO NOTHING";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    
}
