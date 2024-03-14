package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class Gather {
    private static final Logger logger = LoggerFactory.getLogger(Gather.class);

    public static void main(String[] args) {

        args(args);
        String query1 = "select count(*) from huge";
        String query2 = "select sum(col_1) from huge union all select count(*) from huge";
        requireData("select 1 from huge", "myTests/Tables.sql");

        TestUtils.testQueries(logger, new String[]{query1, query2});
    }
}
