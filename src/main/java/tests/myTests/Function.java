package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;
import static bench.V2.sql;

public class Function {
    //TODO add vacuum analyze for generate_series()

    private static final Logger logger = LoggerFactory.getLogger(Function.class);

    public static void main(String[] args) {

        args(args);
        String query1 = "select * from generate_series(1, 5)";
        String query2 = "select * from generate_series(1, 100000)";

        TestUtils.testQueries(logger, new String[]{query1, query2});
    }
}
