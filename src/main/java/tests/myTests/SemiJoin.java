package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.args;
import static bench.V2.requireData;

public class SemiJoin {

    private static final Logger logger = LoggerFactory.getLogger(SemiJoin.class);

    //TODO add some examples with tables. (1, 1), (1, N) , (N, N)
    public static void main(String[] args) {

        args(args);
        String query1 = "select * from small where exists (select * from " +
                "medium where medium.col_1 = small.col_1)";
        String query2 = "select * from large where large.col_1 in " +
                "(select huge.col_1 from huge)";


        requireData("select 1 from medium union all " +
                "select 1 from small union all " +
                "select 1 from large union all " +
                "select 1 from huge", "myTests/Tables.sql");

        TestUtils.testQueries(logger, new String[]{query1, query2});
    }
}
