package tests.operations.utils;


import java.util.ArrayList;

import static bench.V2.args;

public class TestCmd {

    public static String testArgs(String[] args) {
        ArrayList<String> argsNew = new ArrayList<>();
        String type = "all";
        for (String arg : args) {
            if (arg.startsWith("--")) {
                type = arg.substring(2);
            } else {
                argsNew.add(arg);
            }
        }
        args(argsNew.toArray(new String[0]));
        return type;
    }
}
