package operations;

import operations.testplan.TestPlan;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import static bench.V2.*;

public class TestHashSetOp extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestHashSetOp.class);
    private static final String expectedPlanType = "SetOp";
    private static final String planElementName = "Strategy";
    private static final String expectedPlanElement = "Hashed";

    @Test(alwaysRun = true)
    public void runSmallTablesTests() {
        
        String query1 = "select * from huge_table intersect select * from small_table";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runMediumTablesTests() {
        
        String query1 = "select * from huge_table intersect select * from medium_table";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test(alwaysRun = true)
    public void runLargeTablesTests() {
        
        String query1 = "select * from huge_table_with_dups intersect select * from large_table";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
