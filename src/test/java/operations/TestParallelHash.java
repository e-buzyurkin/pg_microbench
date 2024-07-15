package operations;

import bench.V2;
import operations.testplan.TestPlan;
import operations.utils.RequiredData;
import operations.utils.TestUtils;

import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestParallelHash extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestParallelHash.class);
    private static final String expectedPlanType = "Hash";
    private static final String planElementName = "Parallel Aware";
    private static final String expectedPlanElement = "true";

    @Test(alwaysRun = true, priority = 5)
    public void runHugeTablesTests() {
        
        //1:N
        String query1 = "select count(*) from huge_parent_table inner join huge_child_table on" +
                " huge_parent_table.id = huge_child_table.parent_id";
        //1:1
        String query2 = "select count(*) from huge_profile_table inner join huge_users_table on" +
                " huge_profile_table.id = huge_users_table.id";
        //N:N
        String query3 = "select count(*) from huge_business_table sb inner join " +
                "huge_first_partner_table fb on sb.first_partner = fb.id inner join " +
                "huge_second_partner_table sp ON sb.second_partner = sp.id";
        //You can remove 'requireData' if checking query takes too much time and you sure you've created
        // all needed tables.
        V2.requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1, query2, query3};
        TestUtils.testQueriesOnPlanAndPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }

    
}
