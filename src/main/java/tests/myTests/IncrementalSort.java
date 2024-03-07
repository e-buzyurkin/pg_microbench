package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class IncrementalSort {
    private static final Logger logger = LoggerFactory.getLogger(IncrementalSort.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table large(x) as select generate_series(1, 1000000)");
        sql("analyze large");
        sql("create index i_large on large(x)");
        sql("alter table large add column y integer");

        parallel((state) -> {
            sql("select * from large order by x, y");
        });
        sql("drop table large");
    }

}
