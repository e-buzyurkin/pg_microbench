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

    public static void testQueries(Logger logger, String planType, String [] queries, Object... binds) {
        Arrays.stream(queries).forEach(query -> testQuery(logger, planType, query, binds));
    }

    public static void testQuery(Logger logger, String expectedPlanType, String query, Object... binds) {
        JsonObject resultsJson = explainResultsJson(query, binds);
        String actualPlanType = resultsJson.getAsJsonObject("Plan").
                get("Node Type").getAsString();
        String executionTime = resultsJson.get("Execution Time").getAsString();

        try {
            Assertions.assertEquals(expectedPlanType, actualPlanType);
            logger.info("Sql query completed after " + executionTime + " ms");
            parallel((state) -> {
                sql(query);
            });
        } catch (AssertionError e) {
            logger.error(String.valueOf(e));
        }
    }

    public static JsonObject explainResultsJson(String sql, Object... binds) {
        List<PGobject> pGobjectList = select("explain (analyze, verbose, buffers, costs off, format json) " + sql, binds);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(pGobjectList.get(0).getValue(), JsonArray.class);
        return jsonArray.get(0).getAsJsonObject();
    }
}
