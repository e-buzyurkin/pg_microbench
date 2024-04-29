package tests.myTests;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.args;

public class TestTableFunctionScan {
    private static final Logger logger = LoggerFactory.getLogger(TestTableFunctionScan.class);
    private static final String expectedPlanType = "Table Function Scan";

    @Test
    public void runFunctionsTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select * from XMLTABLE(" +
                "'/ROWS/ROW' PASSING " +
                "$$" +
                "<ROWS> <ROW id=\"1\"> <COUNTRY_ID>US</COUNTRY_ID> </ROW> </ROWS>" +
                "$$" +
                "COLUMNS id int PATH '@id', _id FOR ORDINALITY)";
        String[] queries = new String[]{query1};
        TestUtils.testQueriesOnMainPlan(logger, queries, expectedPlanType);
    }
}
