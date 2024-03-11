package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static bench.V2.*;

public class Aggregate {
    private static final Logger logger = LoggerFactory.getLogger(Aggregate.class);

    public static void main(String[] args) {

        args(args);
        String query1 = "select count(*) from aggregate";
        String query2 = "select count(col_2) from aggregate group col_1";
        requireData("select 1 from aggregate", "myTests/Aggregate.sql");
        explain(logger, query1);
        explain(logger, query2);
        parallel((state) -> {
                sql(query1);
                sql(query2);
        });
    }
}
