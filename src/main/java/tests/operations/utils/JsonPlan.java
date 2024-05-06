package tests.operations.utils;

import com.google.gson.JsonObject;

public class JsonPlan {
    private final String plan;
    private final JsonObject json;

    JsonPlan(String plan, JsonObject json) {
        this.plan = plan;
        this.json = json;
    }

    public String getPlanElement() {
        return plan;
    }

    public JsonObject getJson() {
        return json;
    }
}
