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

public class TestUnique extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestUnique.class);
    private static final String expectedPlanType = "Unique";

    @Test
    public void runFunctionTests() {
        
        String query1 = "select distinct * from generate_series(1, 100) order by 1";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        
        String query1 = "select distinct * from medium_table order by 1";
        V2.requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        
        String query1 = "select distinct * from large_table order by 1";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }


}
