package tests.basic;

import bench.V2;

import java.util.HashMap;

public class FastTruncate {
    private static final String GET_CONFIG_VALUE_QUERY = "SELECT param_value FROM config_params WHERE param_name = ?;";

    private static final String[] CFG_PARAMS = {
            "temp_tables_count",
            "test_iter_count",
            "table_rows_count"
    };

    private static final String TBL_NAME = "tt%d";

    private static final String TRUNCATE_CMD = "SELECT fasttruncate('%1$s');";

    private static final String INSERT_CMD = "INSERT INTO %1$s (val, c1, c2, c3) " +
            " SELECT repeat('x', 50)::text AS val, md5(id::text || 'name') AS c1" +
            " , id / 100 AS c2, mod(id, 100) AS c3" +
            " FROM generate_series(1, %2$d) id;";

    private static final String TMP_CMDS_TMPL =
            "CREATE TEMPORARY TABLE IF NOT EXISTS %1$s " +
                    "(id bigserial primary key, val text, c1 text, c2 int, c3 int) ON COMMIT DELETE ROWS;" +
            "CREATE INDEX IF NOT EXISTS %1$s_val_idx ON %1$s(val);" +
            "CREATE INDEX IF NOT EXISTS %1$s_c1_idx ON %1$s(c1);" +
                    TRUNCATE_CMD +
                    INSERT_CMD;

    private static void runTest(String[] args) {
        V2.args(args);
        HashMap<String,String> cfg = new HashMap<>();

        /* initialize HashMap with configuration parameters */
        for(String p: CFG_PARAMS) {;
            String val = V2.selectOne(GET_CONFIG_VALUE_QUERY, p);

            if (val == null) {
                throw new RuntimeException(String.format("%s can't be null", p));
            }

            cfg.put(p, val);
        }

        final int tempTablesCount = Integer.parseInt(cfg.get(CFG_PARAMS[0]));
        final int testIterCount = Integer.parseInt(cfg.get(CFG_PARAMS[1]));
        final int tableRowsCount = Integer.parseInt(cfg.get(CFG_PARAMS[2]));;
        final String TMP_CMDS = String.format(TMP_CMDS_TMPL, "%1$s", tableRowsCount);

        V2.parallel((state) -> {
            for(int i = 0; i < tempTablesCount; i++) {
                String tblName = String.format(TBL_NAME, i);

                V2.sql(String.format(TMP_CMDS, tblName));

                for(int j = 0; j < testIterCount; j++) {
                    V2.sql(String.format(TRUNCATE_CMD, tblName));
                }
            }
        });
    }

    public static void main(String[] args) {
        runTest(args);
    }
}
