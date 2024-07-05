package operations.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProbeData {
    private String type;
    private final Map<String, HashMap<String, Integer>> data = new HashMap<>();

    public Map<String, Integer> getKeyData(String key) {
        return data.get(key);
    }

    @Override
    public String toString() {
        return "Probe{" +
                "\"type\" : " + type + ", " +
                "\"data\" : " + data +
                '}';
    }
}
