package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class SubqueryScanAndHashSetOp {
    private static final Logger logger = LoggerFactory.getLogger(SubqueryScanAndHashSetOp.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";

        sql("create table small (x) as " +
                "select generate_series(1, 1000)");
        sql("analyze small");

        parallel((state) -> {
            sql("select * from small except select * from small");
        });

        sql("drop table small");
    }
}
