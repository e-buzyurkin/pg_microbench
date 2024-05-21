package tests.basic;

import static bench.V2.*;

import bench.v2.WorkerUnit;

public class TempOpFastTruncateNoData {
    public static void main(String[] args) {
        args(args);
        verbosity = true;
        
        WorkerUnit test = (st) -> {
            String tblName = "tt_" + Integer.toString(0);
            sql("CREATE TEMPORARY TABLE IF NOT EXISTS " + tblName + 
                    "(id bigserial primary key, val text, c1 text) ON COMMIT DELETE ROWS");
            sql("CREATE INDEX IF NOT EXISTS " + tblName + "_val_idx ON " + tblName + "(val)");
            sql("CREATE INDEX IF NOT EXISTS " + tblName + "_c1_idx ON " + tblName + "(c1)");

            for (int i = 0; i < 10; i++) {
                sql("SELECT fasttruncate('" + tblName + "')");
            }
        };
        
        parallel(test);
    }
}
