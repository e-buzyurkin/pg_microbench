package operations.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;

public class QueryProfiler {
    private final HashMap<String, Double> profileDataMap = new HashMap<>();

    public void profile(Logger logger) {
        String filePath = "bg.log";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            ObjectMapper objectMapper = new ObjectMapper();
            String line;
            Double allTime = 0.0;

            while ((line = reader.readLine()) != null) {
                try {
                    if (!line.startsWith("{")) {
                        continue;
                    }
                    ProbeData probeData = objectMapper.readValue(line, ProbeData.class);

                    if (probeData.getType().equals("query_start")) {
                        continue;
                    }

                    if (probeData.getType().equals("parse")
                    || probeData.getType().equals("plan") || probeData.getType().equals("rewrite")
                    || probeData.getType().equals("execute")) {
                        allTime += addToHashMap("other", probeData.getTimeBefore());
                    }
                    allTime += addToHashMap(probeData.getType(), probeData.getTimeIn());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("profiling started");
            for (String key : profileDataMap.keySet()) {
                logger.info("{}: {} ms - {}%", key, profileDataMap.get(key),
                        (profileDataMap.get(key)) / allTime * 100);
            }
        } catch (IOException e) {
            logger.info("profiling failed due to {}", e.getMessage());
        }
    }

    private Double addToHashMap(String type, Long time) {
        Double micros = time / 1000.0;
        if (profileDataMap.containsKey(type)) {
            profileDataMap.put(type, profileDataMap.get(type) + micros);
        } else {
            profileDataMap.put(type, micros);
        }
        return micros;
    }
}