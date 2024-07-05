package operations.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import operations.bpftrace.BpfTraceConf;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryProfiler {
    private final HashMap<String, Double> profileDataMap = new HashMap<>();
    private final List<String> phases = new ArrayList<>();
    private Double allTime = 0.0;

    public void profile(Logger logger) {
        String filePath = BpfTraceConf.getLocalLogFile();

        phases.add("@parse");
        phases.add("@rewrite");
        phases.add("@plan");
        phases.add("@execute");
        phases.add("@other");
        phases.add("@between_phases");
        parse(filePath);
        logger.info("profiling started");
        for (String key : phases) {
            if (profileDataMap.containsKey(key.replace("@", ""))) {
                logging(logger, key.replace("@", ""), allTime);
            }
        }
        logger.info("Query execution phases:");
        for (String key : profileDataMap.keySet()) {
            if (!phases.contains("@" + key)) {
                logging(logger, key, profileDataMap.get("execute"));
            }
        }
    }

    private void parse(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            JsonFactory factory = new JsonFactory();
            factory.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
            ObjectMapper objectMapper = new ObjectMapper(factory);
            while ((line = reader.readLine()) != null) {
                line = sanitizeJsonString(line);
                if (line.contains("attached_probes") || line.isEmpty()) {
                    continue;
                }
                ProbeData probe = objectMapper.readValue(line, ProbeData.class);
                for (String phase : phases) {
                    parseExecutePhases(probe, phase);
                }
                Map<String, Integer> nodeMap = probe.getKeyData("@node");
                if (nodeMap != null) {
                    for (String key : nodeMap.keySet()) {
                        addToHashMap(key.split(",")[1].split(":")[2].
                                        replace("Exec", ""),
                                nodeMap.get(key));
                    }
                }
            }
        } catch (IOException e) {
            //clearFile(filePath);
            throw new RuntimeException(e);
        }
    }

    private Double addToHashMap(String type, Integer time) {
        Double ms = time / 1000.0;
        if (profileDataMap.containsKey(type)) {
            profileDataMap.put(type, profileDataMap.get(type) + ms);
        } else {
            profileDataMap.put(type, ms);
        }
        return ms;
    }

    private void parseExecutePhases(ProbeData probe, String phase) {
        if (probe.getKeyData(phase) != null) {
            for (String key : probe.getKeyData(phase).keySet()) {
                allTime += addToHashMap(phase.replace("@", ""), probe.getKeyData(phase).
                        get(key));
            }
        }
    }

    private void logging(Logger logger, String key, Double allTime) {
        logger.info("{}: {} ms - {}%", key, profileDataMap.get(key),
                (profileDataMap.get(key)) / allTime * 100);
    }

    private String sanitizeJsonString(String json) {
        // Удаление всех управляющих символов, кроме \r, \n, \t
        return json.replaceAll("[\\x00-\\x1F&&[^\r\n\t]]", "");
    }
}
