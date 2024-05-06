package tests.operations;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.operations.utils.RequiredData;
import tests.operations.utils.TestUtils;

import static bench.V2.*;

public class TestSetOp {
    private static final Logger logger = LoggerFactory.getLogger(TestSetOp.class);
    private static final String expectedPlanType = "SetOp";

    @Test
    public void runHugeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from huge_table intersect select * from huge_table";
        requireData(RequiredData.checkTables("huge"), "tests/operations/HugeTables.sql");
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table large(x) as select generate_series(1, 1000000)");
        sql("analyze large");

        parallel((state) -> {
            sql("select * from large intersect select * from large");
        });
        sql("drop table large");
    }
}
