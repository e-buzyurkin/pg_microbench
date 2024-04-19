package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class TestFunction {

    private static final Logger logger = LoggerFactory.getLogger(TestFunction.class);

    @Test
    public void runFunctionTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from generate_series(1, 5)";
        String query2 = "select * from generate_series(1, 100000)";
        String[] queries = new String[]{query1, query2};
        TestUtils.testQueries(logger, queries, "Function");
    }
}
