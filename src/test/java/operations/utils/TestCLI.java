package operations.utils;

import static bench.V2.args;

public class TestCLI {

    public static void connectToDatabase() {
        String[] args = System.getProperty("args").split("\\s+");
        args(args);
    }

}
