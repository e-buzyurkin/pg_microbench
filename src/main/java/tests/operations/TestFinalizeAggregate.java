package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestFinalizeAggregate {

    private static final Logger logger = LoggerFactory.getLogger(TestParallelHashJoin.class);
    private static final String expectedPlanType = "Aggregate";
    private static final String planElementName = "Partial Mode";
    private static final String expectedPlanElement = "Finalize";

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
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
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1, query2, query3};
        TestUtils.testQueriesOnPlanElement(logger, queries, expectedPlanType, planElementName, expectedPlanElement);
    }
}
