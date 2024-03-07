package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class Function {
    private static final Logger logger = LoggerFactory.getLogger(Function.class);

    public static void main(String[] args) {

        args(args);
        String query = "select * from generate_series(1, 4)";
        parallel((state) -> {
            sql(query);
        });
        explain(logger, query);
    }
}
