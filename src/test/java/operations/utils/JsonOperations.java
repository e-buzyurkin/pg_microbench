package operations.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.postgresql.util.PGobject;

import java.util.ArrayList;
import java.util.List;

import static bench.V2.select;

public class JsonOperations {

    public static JsonObject explainResultsJson(String sql, Object... binds) {
        List<PGobject> pGobjectList = select("explain (analyze, verbose, buffers, costs off, format json) " + sql, binds);
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(pGobjectList.get(0).getValue(), JsonArray.class);
        return jsonArray.get(0).getAsJsonObject();
    }

    public static List<JsonPlan> getAllPlans(JsonObject jsonObject) {
        return getAllPlansRecursive(jsonObject, new ArrayList<>());
    }

    private static List<JsonPlan> getAllPlansRecursive(JsonObject jsonObject, List<JsonPlan> list) {
        list.add(new JsonPlan(jsonObject.get("Node Type").getAsString(), jsonObject));
        if (jsonObject.getAsJsonArray("Plans") != null) {
            JsonArray jsonArray = jsonObject.getAsJsonArray("Plans");
            for (JsonElement jsonElement : jsonArray) {
                return getAllPlansRecursive(jsonElement.getAsJsonObject(), list);
            }
        }
        return list;
    }

    private static JsonPlan findPlanElementRecursive(JsonObject jsonObject, String key, String planElement) {
        JsonPlan result = new JsonPlan("", jsonObject);
        if (jsonObject.has(key)) {
            result = new JsonPlan(jsonObject.get(key).getAsString(), jsonObject);
        }
        if (jsonObject.getAsJsonArray("Plans") != null) {
            String curPlanElement = "";
            if (jsonObject.has(key)) {
                curPlanElement = jsonObject.get(key).getAsString();
            }
            if (curPlanElement.equals(planElement)) {
                return new JsonPlan(curPlanElement, jsonObject);
            }
            JsonArray jsonArray = jsonObject.getAsJsonArray("Plans");
            for (JsonElement jsonElement : jsonArray) {
                result = findPlanElementRecursive(jsonElement.getAsJsonObject(), key, planElement);
                if (result.getPlanElement().equals(planElement)) {
                    return result;
                }
            }
        }
        return result;
    }

    public static JsonPlan findPlanElement(JsonObject jsonObject, String key, String element) {
        return findPlanElementRecursive(jsonObject.getAsJsonObject("Plan"), key, element);
    }

}
