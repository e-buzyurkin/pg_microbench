package tests.myTests.testUtils;
import static bench.V2.*;

public class DropTables {

    public static void main(String[] args) {
        args(args);
        requireData("error;", "myTests/DropTables.sql");
    }

    public static void dropTables() {
        requireData("error", "myTests/DropTables.sql");
    }

    public static void dropTableGroup(String tableGroup) {
        if (tableGroup.equals("small")) {
            requireData("error;", "myTests/DropSmallTables.sql");
        }
        if (tableGroup.equals("medium")) {
            requireData("error;", "myTests/DropMediumTables.sql");
        }
        if (tableGroup.equals("large")) {
            requireData("error;", "myTests/DropLargeTables.sql");
        }
        if (tableGroup.equals("huge")) {
            requireData("error;", "myTests/DropHugeTables.sql");
        }
    }
}
