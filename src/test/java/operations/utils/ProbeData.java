package operations.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProbeData {
    @JsonProperty("process")
    private Long process;

    @JsonProperty("type")
    private String type;

    @JsonProperty("query_type")
    private String queryType;

    @JsonProperty("time_before")
    private Long timeBefore;

    @JsonProperty("time_in")
    private Long timeIn;

    @JsonProperty("statement")
    private String statement;

    @JsonProperty("op_counter")
    private Long opCounter;

    @Override
    public String toString() {
        return "ProbeData {" +
                "\"process\" : " + process
                + "\"type\" : \"" + type + "\""
                + "\"queryType\" : \"" + queryType + "\""
                + ", \"timeBefore\" : " + timeBefore
                + ", \"timeIn\" : " + timeIn
                + ", \"statement\" : \"" + statement + "\""
                + ", \"opCounter\" : " + opCounter;
    }
}
