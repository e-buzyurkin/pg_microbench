package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;
import static bench.V2.sql;

public class GroupAggregate {
    private static final Logger logger = LoggerFactory.getLogger(GroupAggregate.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table medium(x) as select generate_series(1, 100000)");
        parallel((state) -> {
            sql("select x, count(*) from medium group by x order by x");
        });
        sql("drop table medium");
    }
}
