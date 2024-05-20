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

public class TestMaterialize extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestMaterialize.class);
    private static final String expectedPlanType = "Materialize";

    @Test
    public void runSmallTablesTests() {
        
        String query1 = "select * from small_table s1, small_table s2 where s1.x != s2.x";
        V2.requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnSubPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        
        String query1 = "select * from medium_table s1, medium_table s2 where s1.x != s2.x";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnSubPlan(logger, queries, expectedPlanType);
    }

    
}
