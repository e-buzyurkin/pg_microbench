package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class Aggregate {
    private static final Logger logger = LoggerFactory.getLogger(Aggregate.class);

    public static void main(String[] args) {

        args(args);
        String query1 = "select count(*) from small";
        String query2 = "select count(*) from medium";
        String query3 = "select count(*) from large";
        requireData("select 1 from small union all " +
                "select 1 from medium union all " +
                "select 1 from large", "myTests/Tables.sql");

        TestUtils.testQueries(logger, new String[]{query1, query2, query3});
    }
}
