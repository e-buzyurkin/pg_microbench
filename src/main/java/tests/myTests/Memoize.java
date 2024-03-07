package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class Memoize {
    private static final Logger logger = LoggerFactory.getLogger(Memoize.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table small_with_dups (x) as " +
                "select generate_series(1, 1000) " +
                "from generate_series(1, 10)");
        sql("create table medium(x) as " +
                "select generate_series(1, 100000)");
        sql("create index i_medium on medium(x)");
        sql("analyze");

        parallel((state) -> {
            sql("select * from small_with_dups " +
                    "join medium using(x)");
        });

        sql("drop index i_medium");
        sql("drop table small_with_dups");
        sql("drop table medium");
    }
}
