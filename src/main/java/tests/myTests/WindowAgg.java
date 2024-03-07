package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class WindowAgg {
    private static final Logger logger = LoggerFactory.getLogger(WindowAgg.class);

    public static void main(String[] args) {

        args(args);
        String query = "select x, sum(x) over() from generate_series(1, 10) as f(x)";

        parallel((state) -> {
            sql(query);
        });
        explain(logger, query);
    }
}
