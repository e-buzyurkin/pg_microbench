package tests.myTests;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.myTests.testUtils.RequiredData;
import tests.myTests.testUtils.TestUtils;

import static bench.V2.*;



public class TestAggregate {
    private static final Logger logger = LoggerFactory.getLogger(TestAggregate.class);

    @Test
    public void runSmallTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select count(*) from small_table";
        requireData(RequiredData.checkTables("small"), "myTests/SmallTables.sql");
        TestUtils.testQueries(logger, TestAggregate.class.getSimpleName().replace("Test", ""), new String[]{query1});
    }

    @Test
    public void runMediumTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select count(*) from medium_table";
        requireData(RequiredData.checkTables("medium"), "myTests/MediumTables.sql");

        TestUtils.testQueries(logger, TestAggregate.class.getSimpleName().replace("Test", ""), new String[]{query1});
    }

    @Test
    public void runLargeTablesTests() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
        String query1 = "select count(*) from large_table";
        requireData(RequiredData.checkTables("large"), "myTests/LargeTables.sql");

        TestUtils.testQueries(logger, TestAggregate.class.getSimpleName(), new String[]{query1});
    }

}
