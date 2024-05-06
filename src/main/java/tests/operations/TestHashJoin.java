package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestHashJoin {

    private static final Logger logger = LoggerFactory.getLogger(TestHashJoin.class);
    private static final String expectedPlanType = "Hash Join";

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);

        String query1 = "select * from small_table_1 where exists (select * from " +
                "small_table_2 where small_table_1.x = small_table_2.x)";
        //1:N relation
        String query2 = "select * from small_parent_table inner join small_child_table on" +
                " small_parent_table.id = small_child_table.parent_id";
        //1:1 relation
        String query3 = "select * from small_profile_table inner join small_users_table on" +
                " small_profile_table.id = small_users_table.id";
        //N:N relation
        String query4 = "select * from small_business_table sb inner join " +
                "small_first_partner_table sfb on sb.first_partner = sfb.id inner join " +
                "small_second_partner_table ssp ON sb.second_partner = ssp.id";
        requireData(RequiredData.checkTables("small"), "tests/operations/SmallTables.sql");
        String[] queries = new String[]{query1, query2, query3, query4};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_table_1 where exists (select * from " +
                "medium_table_2 where medium_table_1.x = medium_table_2.x)";
        //1:N relation
        String query2 = "select * from medium_parent_table inner join medium_child_table on" +
                " medium_parent_table.id = medium_child_table.parent_id";
        //1:1 relation
        String query3 = "select * from medium_profile_table inner join medium_users_table on" +
                " medium_profile_table.id = medium_users_table.id";
        //N:N relation
        String query4 = "select * from medium_business_table sb inner join " +
                "medium_first_partner_table fb on sb.first_partner = fb.id inner join " +
                "medium_second_partner_table sp ON sb.second_partner = sp.id";
        requireData(RequiredData.checkTables("medium"), "tests/operations/MediumTables.sql");
        String[] queries = new String[]{query1, query2, query3, query4};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);


        String query1 = "select * from large_table_1 where exists (select * from " +
                "large_table_2 where large_table_1.x = large_table_2.x)";
        //1:N relation
        String query2 = "select * from large_parent_table inner join large_child_table on" +
                " large_parent_table.id = large_child_table.parent_id";
        //1:1 relation
        String query3 = "select * from large_profile_table inner join large_users_table on" +
                " large_profile_table.id = large_users_table.id";
        //N:N relation
        String query4 = "select * from large_business_table sb inner join " +
                "large_first_partner_table fb on sb.first_partner = fb.id inner join " +
                "large_second_partner_table sp ON sb.second_partner = sp.id";
        requireData(RequiredData.checkTables("large"), "tests/operations/LargeTables.sql");
        String[] queries = new String[]{query1, query2, query3, query4};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

}
