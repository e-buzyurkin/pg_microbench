package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class MixedAggregate {
    private static final Logger logger = LoggerFactory.getLogger(MixedAggregate.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table medium(x) as select generate_series(1, 100000)");
        parallel((state) -> {
            sql("select distinct x from medium group by rollup(x)");
        });
    }
}
