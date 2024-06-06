package operations.utils;

import com.google.gson.JsonObject;
import lombok.Getter;

public class JsonPlan {
    private final String plan;
    @Getter
    private final JsonObject json;

    JsonPlan(String plan, JsonObject json) {
        this.plan = plan;
        this.json = json;
    }

    public String getPlanElement() {
        return plan;
    }

}
