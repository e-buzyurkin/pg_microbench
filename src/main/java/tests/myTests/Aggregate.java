package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class Aggregate {
    private static final Logger logger = LoggerFactory.getLogger(Aggregate.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        parallel((state) -> {
            requireData(query, () -> {
                sql("create table medium(x) as select generate_series(1, 100000)");
                return null;
            });
        });
        explain(logger, query);
    }
}
