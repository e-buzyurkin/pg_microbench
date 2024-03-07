package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class Materialize {
    private static final Logger logger = LoggerFactory.getLogger(Materialize.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table small (x) as " +
                "select generate_series(1, 1000)");
        sql("analyze small");

        parallel((state) -> {
            sql("select * from small s1, small s2 where s1.x != s2.x");
        });

        sql("drop table small");
    }
}
