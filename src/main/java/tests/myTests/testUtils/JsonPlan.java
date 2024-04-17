package tests.myTests.testUtils;

import com.google.gson.JsonObject;

public class JsonPlan {
    private final String plan;
    private final JsonObject json;

    JsonPlan(String plan, JsonObject json) {
        this.plan = plan;
        this.json = json;
    }

    public String getPlan() {
        return plan;
    }

    public JsonObject getJson() {
        return json;
    }
}
