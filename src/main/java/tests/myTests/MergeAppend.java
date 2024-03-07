package tests.myTests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static bench.V2.*;

public class MergeAppend {
    private static final Logger logger = LoggerFactory.getLogger(MergeAppend.class);

    public static void main(String[] args) {

        args(args);
        String query = "(values (1), (2) order by 1) union all" +
                "(values(3), (4) order by 1)" +
                "order by 1";

        parallel((state) -> {
            sql(query);
        });
        explain(logger, query);
    }
}
