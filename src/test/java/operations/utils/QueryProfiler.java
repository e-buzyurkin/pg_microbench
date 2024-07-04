package operations.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import operations.bpftrace.BpfTraceConf;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueryProfiler {
    private final HashMap<String, Double> profileDataMap = new HashMap<>();

    public void profile(Logger logger) {
        String filePath = BpfTraceConf.getLocalLogFile();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            ObjectMapper objectMapper = new ObjectMapper();
            String line;
            Double allTime = 0.0;
            List<String> phases = new ArrayList<>();
            phases.add("parse");
            phases.add("rewrite");
            phases.add("plan");
            phases.add("execute");
            phases.add("other");

            while ((line = reader.readLine()) != null) {
                try {
                    if (!line.startsWith("{")) {
                        continue;
                    }
                    ProbeData probeData = objectMapper.readValue(line, ProbeData.class);

                    if (probeData.getType().equals("query_start")) {
                        continue;
                    }

                    if (phases.contains(probeData.getType())) {
                        allTime += addToHashMap("other", probeData.getTimeBefore());
                        allTime += addToHashMap(probeData.getType(), probeData.getTimeIn());

                    } else {
                        probeData.setType((probeData.getType().split(":"))[2].replace("Exec", ""));
                        addToHashMap(probeData.getType(), probeData.getTimeIn());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("profiling started");
            for (String key : phases) {
                if (profileDataMap.containsKey(key)) {
                    logging(logger, key, allTime);
                }
            }
            logger.info("Query execution phases:");
            for (String key : profileDataMap.keySet()) {
                if (!phases.contains(key)) {
                    logging(logger, key, profileDataMap.get("execute"));
                }
            }
        } catch (IOException e) {
            logger.info("profiling failed due to {}", e.getMessage());
        }
        clearFile(filePath);
    }

    private Double addToHashMap(String type, Long time) {
        Double ms = time / 1000.0;
        if (profileDataMap.containsKey(type)) {
            profileDataMap.put(type, profileDataMap.get(type) + ms);
        } else {
            profileDataMap.put(type, ms);
        }
        return ms;
    }

    private void clearFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Writing an empty string to the file to clear its contents
            writer.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logging(Logger logger, String key, Double allTime) {
        logger.info("{}: {} ms - {}%", key, profileDataMap.get(key),
                (profileDataMap.get(key)) / allTime * 100);
    }
}
