package operations.utils;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QueryProfileData {
    private String operationName;
    private double seconds;
    private double percent;

    QueryProfileData(String operationName, double seconds) {
        this.operationName = operationName;
        this.seconds = seconds;
    }

    public void addSeconds(double newSeconds) {
        seconds += newSeconds;
    }
}
