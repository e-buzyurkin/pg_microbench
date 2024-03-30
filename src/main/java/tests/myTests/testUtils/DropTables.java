package tests.myTests.testUtils;
import static bench.V2.*;

public class DropTables {

    public static void main(String[] args) {
        args(args);
        requireData("error;", "myTests/DropTables.sql");
    }
}
