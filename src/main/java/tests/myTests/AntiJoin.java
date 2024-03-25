package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.args;
import static bench.V2.requireData;

public class AntiJoin {
    private static final Logger logger = LoggerFactory.getLogger(AntiJoin.class);

    //TODO add some examples with tables. (1, 1), (1, N) , (N, N)
    public static void main(String[] args) {

        args(args);
        String query1 = "select * from small where not exists (select * from " +
                "medium where medium.col_1 = small.col_1)";

        requireData("select 1 from medium union all " +
                "select 1 from small", "myTests/Tables.sql");

        TestUtils.testQueries(logger, AntiJoin.class.getSimpleName(), new String[]{query1});
    }
}
