package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.DropTables;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.args;
import static bench.V2.requireData;

public class TestDelete {
    private static final Logger logger = LoggerFactory.getLogger(TestDelete.class);
    private static final String expectedPlanType = "ModifyTable";
    private static final String planElementName = "Operation";
    private static final String expectedPlanElement = "Delete";

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "delete from small_table";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
        DropTables.dropTableGroup("small");
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "delete from medium_table";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
        DropTables.dropTableGroup("medium");
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "delete from large_table";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
        DropTables.dropTableGroup("large");
    }

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "delete from huge_table";
        requireData(RequiredData.checkTables("huge"), "myTests/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
        DropTables.dropTableGroup("huge");
    }
}
