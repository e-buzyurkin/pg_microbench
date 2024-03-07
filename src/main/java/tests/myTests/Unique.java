package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class Unique {
    private static final Logger logger = LoggerFactory.getLogger(Unique.class);

    public static void main(String[] args) {

        args(args);
        String query = "select distinct * from generate_series(1, 10) order by 1";

        parallel((state) -> {
            sql(query);
        });
        explain(logger, query);
    }
}
