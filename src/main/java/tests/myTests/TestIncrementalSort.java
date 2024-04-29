package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class TestIncrementalSort {
    private static final Logger logger = LoggerFactory.getLogger(TestIncrementalSort.class);

    private static final String expectedPlanType = "IncrementalSort";

    //TODO create new <size>_index_table with x and y as columns.
    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from small_index_table order by x, y";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from medium_index_table order by x, y";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from large_index_table order by x, y";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table large(x) as select generate_series(1, 1000000)");
        sql("analyze large");
        sql("create index i_large on large(x)");
        sql("alter table large add column y integer");

        parallel((state) -> {
            sql("select * from large order by x, y");
        });
        sql("drop table large");
    }

}
