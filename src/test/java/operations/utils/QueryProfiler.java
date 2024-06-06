package operations.utils;

import com.google.gson.JsonObject;
import static operations.utils.JsonOperations.*;

import java.util.HashMap;
import java.util.List;

public class QueryProfiler {
    private static HashMap<Integer, QueryProfileData> profileDataList;
    private double time;

    public void profile(String query, Object ...binds) {
        JsonObject object = explainResultsJson(query, binds);
        List<JsonPlan> list = getAllPlans(object);
        for (int i = 0; i < list.size(); i++) {
            double seconds = list.get(i).getJson().get("Actual Total Time").getAsDouble();
            time += seconds;
            if (profileDataList.containsKey(i)) {
                profileDataList.get(i).addSeconds(seconds);
            }
            else {
                profileDataList.put(i, new QueryProfileData(list.get(i).getPlanElement(), seconds));
            }
        }
    }

    public HashMap<Integer, QueryProfileData> getProfileDataList() {
        for (QueryProfileData profileData : profileDataList.values()) {
            profileData.setPercent(profileData.getSeconds() / time * 100);
        }
        return profileDataList;
    }
}
