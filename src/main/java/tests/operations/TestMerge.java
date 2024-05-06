package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.args;
import static bench.V2.requireData;

public class TestMerge {
    private static final Logger logger = LoggerFactory.getLogger(TestMerge.class);
    private static final String expectedPlanType = "ModifyTable";
    private static final String planElementName = "Operation";
    private static final String expectedPlanElement = "Merge";

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "merge into small_parent_table using (values (1)) m(id) " +
                "ON small_parent_table.id = m.id " +
                "when not matched then insert (id) values (m.id) when matched then update set data = 'TRUE'";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "merge into medium_parent_table using (values (1)) m(id) " +
                "ON medium_parent_table.id = m.id " +
                "when not matched then insert (id) values (m.id) when matched then update set data = 'TRUE'";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "merge into large_parent_table using (values (1)) m(id) " +
                "ON large_parent_table.id = m.id " +
                "when not matched then insert (id) values (m.id) when matched then update set data = 'TRUE'";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "merge into huge_parent_table using (values (1)) m(id) " +
                "ON huge_parent_table.id = m.id " +
                "when not matched then insert (id) values (m.id) when matched then update set data = 'TRUE'";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
