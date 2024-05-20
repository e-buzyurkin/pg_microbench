package operations.utils;

import static bench.V2.args;
import static bench.V2.db;

public class TestCLI {

    public static void connectToDatabase() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
    }

}
