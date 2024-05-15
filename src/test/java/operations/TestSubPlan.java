package operations;

import bench.V2;
import com.google.gson.JsonObject;
import operations.utils.JsonPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class TestSubPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestSubPlan.class);
    private static final String planElementName = "Parent Relationship";
    private static final String expectedPlanElement = "SubPlan";

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from small_table where small_table.x not in (select small_table.x from small_table)";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, planElementName, expectedPlanElement);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_table where medium_table.x not in (select medium_table.x from medium_table)";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnPlanElement(logger, queries, planElementName, expectedPlanElement);
    }

}
