package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class GatherMerge {
    private static final Logger logger = LoggerFactory.getLogger(GatherMerge.class);

    public static void main(String[] args) {

        args(args);
        String query = "select count(*) from medium";
        sql("create table huge(x) as select generate_series(1, 20000000)");

        parallel((state) -> {
            sql("select * from huge order by 1");
        });
        sql("drop table huge");
    }
}
