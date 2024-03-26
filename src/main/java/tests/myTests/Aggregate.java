package tests.myTests;


import org.junit.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestCmd;
import tests.myTests.testUtils.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.stream.Stream;

import static bench.V2.*;



public class Aggregate {
    private static final Logger logger = LoggerFactory.getLogger(Aggregate.class);

    @Test
    public void runSmallTablesTests() {
        String []args = {"-h", "localhost"};
        args(args);
        String query1 = "select count(*) from small_table";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        TestUtils.getQueryPlanType(query1);
        TestUtils.testQueries(logger, Aggregate.class.getSimpleName(), new String[]{query1});
    }

    public static void runMediumTablesTests() {
        String query1 = "select count(*) from medium_table";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");

        TestUtils.testQueries(logger, Aggregate.class.getSimpleName(), new String[]{query1});
    }


    public static void runLargeTablesTests() {
        String query1 = "select count(*) from large_table";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");

        TestUtils.testQueries(logger, Aggregate.class.getSimpleName(), new String[]{query1});
    }

    private static String[] provideArgumentsFromCommandLine() {
        return System.getProperty("args").split(" ");
    }

    public static void main(String[] args) {

        String type = TestCmd.testArgs(args);
    }
}
