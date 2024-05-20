package operations;

import operations.testplan.TestPlan;
import operations.utils.TestCLI;
import operations.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.args;

public class TestTableFunctionScan extends TestPlan {
    private static final Logger logger = LoggerFactory.getLogger(TestTableFunctionScan.class);
    private static final String expectedPlanType = "Table Function Scan";

    @Test
    public void runFunctionsTests() {
        
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
