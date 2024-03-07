package tests.myTests;

import bench.v2.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class Values {
    private static final Logger logger = LoggerFactory.getLogger(Values.class);

    public static void main(String[] args) {

        args(args);
        String query = "values (1), (2), (3)";

        parallel((state) -> {
            sql(query);
        });
        explain(logger, query);
    }
}
