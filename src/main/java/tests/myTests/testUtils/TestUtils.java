package tests.myTests.testUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;

import javax.management.Query;
import java.util.Arrays;
import java.util.List;

import static bench.V2.*;

public class TestUtils {

    public static void testQueries(Logger logger, String planType, String [] queries, Object... binds) {
        Arrays.stream(queries).forEach(query -> testQuery(logger, planType, query, binds));
    }

    public static void testQuery(Logger logger, String expectedPlanType, String query, Object... binds) {
        String actualPlanType = getQueryPlanType(query, binds);
        assert expectedPlanType.equals(actualPlanType);
        explain(logger, query);
        parallel((state) -> {
            sql(query);
        });
    }

    public static String getQueryPlanType(String sql, Object... binds) {
        List<PGobject> pGobjectList = select("explain (analyze, verbose, buffers, costs off, format json) " + sql, binds);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(pGobjectList.get(0).getValue(), JsonArray.class);
        return jsonArray.get(0).getAsJsonObject().getAsJsonObject("Plan").get("Node Type").getAsString();
    }
}
