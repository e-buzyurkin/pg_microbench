package tests;
import static bench.V2.*;
public class InsertDeleteOnly {
    public static void main(String[] args) {
        args(args);

        requireData("select 1 from t limit 1", "InsertDeleteOnly.sql",0);

        parallel((state) -> {
            sql("insert into t(val)\n" +
                    "select id*random()*(10000000-1)+1 from pg_catalog.generate_series(1, 10, 1) id;");
            sql("delete from t where id = (select id from t limit 1);");
        });
    }
}
