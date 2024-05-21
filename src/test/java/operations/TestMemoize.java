package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class TestMemoize extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestMemoize.class);
    private static final String expectedPlanType = "Memoize";

    @Test(alwaysRun = true)
    public void runMediumLargeTablesTests() {
        
        String query1 = "select * from medium_table_with_dups join large_table using (x)";
        V2.requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        sql("create index if not exists i_large on large_table(x)");
        String[] queries = new String[]{query1};
        try {
            TestUtils.testQueriesOnSubPlan(logger, queries, expectedPlanType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            sql("drop index if exists i_large");
        }
    }

    

}
