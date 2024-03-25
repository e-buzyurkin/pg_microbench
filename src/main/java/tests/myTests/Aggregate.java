package tests.myTests;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestCmd;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;

public class Aggregate {
    private static final Logger logger = LoggerFactory.getLogger(Aggregate.class);

    private static void runSmallTablesTests() {
        String query1 = "select count(*) from small_table";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        TestUtils.getQueryPlanType(query1);

        TestUtils.testQueries(logger, Aggregate.class.getSimpleName(), new String[]{query1});
    }

    private static void runMediumTablesTests() {
        String query1 = "select count(*) from medium_table";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");

        TestUtils.testQueries(logger, Aggregate.class.getSimpleName(), new String[]{query1});
    }

    private static void runLargeTablesTests() {
        String query1 = "select count(*) from large_table";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");

        TestUtils.testQueries(logger, Aggregate.class.getSimpleName(), new String[]{query1});
    }

    public static void main(String[] args) {

        String type = TestCmd.testArgs(args);
        switch (type) {
            case "small":
                runSmallTablesTests();
                break;
            case "medium":
                runMediumTablesTests();
                break;
            case "large":
                runLargeTablesTests();
                break;
            case "all":
                runSmallTablesTests();
                runMediumTablesTests();
                runLargeTablesTests();
        }
    }
}
