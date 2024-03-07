package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class Append {
    private static final Logger logger = LoggerFactory.getLogger(Append.class);

    //TODO add some examples with tables. (1, N) , (N, N)
    public static void main(String[] args) {

        args(args);
        String query = "select 1 union all select 2";
        parallel((state) -> {
            sql(query);
        });
        explain(logger, query);
    }
}
