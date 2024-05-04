package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.args;
import static bench.V2.requireData;

public class TestHashSemiJoin {
    private static final Logger logger = LoggerFactory.getLogger(TestHashSemiJoin.class);
    private static final String expectedPlanType = "Hash Join";
    private static final String planElementName = "Join Type";
    private static final String expectedPlanElement = "Semi";


    @Test
    public void runSmallMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);

        String query1 = "select * from small_table where exists (select * from " +
                "medium_table where small_table.x = medium_table.x)";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runMediumLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_table where exists (select * from " +
                "large_table where medium_table.x = large_table.x)";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);


        String query1 = "select * from large_table where exists (select * from " +
                "huge_table where large_table.x = huge_table.x)";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        requireData(RequiredData.checkTables("huge"), "myTests/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }
}
