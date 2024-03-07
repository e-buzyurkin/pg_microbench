package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class Gather {
    private static final Logger logger = LoggerFactory.getLogger(Gather.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";
        sql("create table large(x) as select generate_series(1, 1000000)");

        parallel((state) -> {
            sql("select sum(x) from large");
        });
        sql("drop table large");
    }
}
