package tests.myTests.testUtils;
import org.slf4j.Logger;

import java.util.Arrays;

import static bench.V2.*;

public class TestUtils {

    public static void testQueries(Logger logger, String [] queries) {
        Arrays.stream(queries).forEach(query -> testQuery(logger, query));
    }

    public static void testQuery(Logger logger, String query) {
        explain(logger, query);
        parallel((state) -> {
            sql(query);
        });
    }
}
