package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;
import static bench.V2.sql;

public class WindowAgg {

    private static final Logger logger = LoggerFactory.getLogger(WindowAgg.class);

    public static void main(String[] args) {

        args(args);
        String query1 = "select x, sum(x) over() from generate_series(1, 10) as f(x)";
        String query2 = "select x, sum(x) over() from large as f(x)";

        requireData("select 1 from large", "myTests/Tables.sql");

        TestUtils.testQueries(logger, new String[]{query1, query2});
    }
}
