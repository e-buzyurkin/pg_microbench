package tests.myTests.testUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.List;

import static bench.V2.*;

public class TestUtils {

    public static void testQuery(Logger logger, String query, Object... binds) {
        parallel((state) -> {
            sql(query);
        });
    }

    public static void checkTime(Logger logger, JsonObject explainResults) {
        String executionTime = explainResults.get("Execution Time").getAsString();
        logger.info("Sql query completed after " + executionTime + " ms");
    }

    public static JsonObject explainResultsJson(String sql, Object... binds) {
        List<PGobject> pGobjectList = select("explain (analyze, verbose, buffers, costs off, format json) " + sql, binds);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(pGobjectList.get(0).getValue(), JsonArray.class);
        return jsonArray.get(0).getAsJsonObject();
    }
}
