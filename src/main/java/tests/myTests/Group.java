package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class Group {
    private static final Logger logger = LoggerFactory.getLogger(Group.class);

    //TODO replace "sql"  and "REQUIRE". Add "Explain".
    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table large(x) as select generate_series(1, 500000)");
        parallel((state) -> {
            sql("select x from large where x < 0 group by x");
        });
        sql("drop table large");
    }
}
