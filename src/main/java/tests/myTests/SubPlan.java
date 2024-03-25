package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.args;
import static bench.V2.requireData;

public class SubPlan {
    private static final Logger logger = LoggerFactory.getLogger(SubPlan.class);

    //TODO add some examples with tables. (1, 1), (1, N) , (N, N)
    public static void main(String[] args) {

        args(args);
        String query1 = "select * from small where small.col_1 not in " +
                "(select medium.col_1 from medium)";

        requireData("select 1 from medium union all " +
                "select 1 from small", "myTests/Tables.sql");

        TestUtils.testQueries(logger, SubPlan.class.getSimpleName(), new String[]{query1});
    }
}
