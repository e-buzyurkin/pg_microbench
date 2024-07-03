package utils;
import static bench.V2.*;

public class DropTables {

    public static void main(String[] args) {
        args(args);
        requireData("error;", "tests/operations/DropTables.sql");
    }

    public static void dropTables() {
        requireData("error", "tests/operations/DropTables.sql");
    }

    public static void dropTableGroup(String tableGroup) {
        if (tableGroup.equals("small")) {
            requireData("error;", "tests/operations/DropSmallTables.sql");
        }
        if (tableGroup.equals("medium")) {
            requireData("error;", "tests/operations/DropMediumTables.sql");
        }
        if (tableGroup.equals("large")) {
            requireData("error;", "tests/operations/DropLargeTables.sql");
        }
        if (tableGroup.equals("huge")) {
            requireData("error;", "tests/operations/DropHugeTables.sql");
        }
    }
}
