package tests.myTests.testUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.jupiter.api.Assertions;
import org.postgresql.util.PGobject;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static JsonPlan findPlanElementRecursive(JsonObject jsonObject, String planElement) {
        JsonPlan result = new JsonPlan(jsonObject.get("Node Type").getAsString(), jsonObject);
        if (jsonObject.getAsJsonArray("Plans") != null) {
            String planType = jsonObject.get("Node Type").getAsString();
            if (planType.equals(planElement)) {
                return new JsonPlan(planType, jsonObject);
            }
            JsonArray jsonArray = jsonObject.getAsJsonArray("Plans");
            for (JsonElement jsonElement: jsonArray) {
                result = findPlanElementRecursive(jsonElement.getAsJsonObject(), planElement);
                if (result.getPlan().equals(planElement)) {
                    return result;
                }
            }
        }
        return result;
    }

    public static JsonPlan findPlanElement(JsonObject jsonObject, String planElement) {
        return findPlanElementRecursive(jsonObject.getAsJsonObject("Plan"), planElement);
    }
}
