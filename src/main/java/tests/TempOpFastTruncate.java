package tests;

import static bench.V2.*;

import bench.v2.WorkerUnit;

public class TempOpFastTruncate {
    public static void main(String[] args) {
        args(args);
        verbosity = true;
        
        WorkerUnit test = (st) -> {
            String tblName = "tt_" + Integer.toString(0);
            sql("CREATE TEMPORARY TABLE IF NOT EXISTS " + tblName + 
                    "(id bigserial primary key, val text, c1 text, c2 int, c3 int) ON COMMIT DELETE ROWS");
            sql("CREATE INDEX IF NOT EXISTS " + tblName + "_val_idx ON " + tblName + "(val)");
            sql("CREATE INDEX IF NOT EXISTS " + tblName + "_c1_idx ON " + tblName + "(c1)");
            
            for (int i = 0; i < 10; i++) {
                sql("INSERT INTO " + tblName + " (val, c1, c2, c3) " +
                        " SELECT repeat('x', 50)::text AS val, md5(id::text || 'name') AS c1" +
                        " , id / 100 AS c2, mod(id, 100) AS c3" +
                        " FROM generate_series(1, 100) id");
                sql("SELECT fasttruncate('" + tblName + "')");
            }
        };
        parallel(test);
    }
}
