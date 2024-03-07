package tests;
import static bench.V2.*;
public class InsertOnly {
    public static void main(String[] args) {
        args(args);

        requireData("select 1 from t limit 1", "InsertOnly.sql",0);

        parallel((state) -> {
            sql("insert into t(val)\n" +
                    "select repeat('x',1023) from pg_catalog.generate_series(1,1000)");
        });
    }
}
