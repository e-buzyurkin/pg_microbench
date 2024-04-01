package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class Append {
    private static final Logger logger = LoggerFactory.getLogger(Append.class);

    public static void main(String[] args) {

        args(args);
        String query1 = "select 1 union all select 2";
        String query2 = "select * from large union all select * from large";
        String query3 = "select 100 from medium union all select * from medium";

        requireData("select 1 from medium union all " +
                "select 1 from large", "myTests/Tables.sql");

    }
}
