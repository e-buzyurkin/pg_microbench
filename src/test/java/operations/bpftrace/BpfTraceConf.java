package operations.bpftrace;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class BpfTraceConf {
    @Getter
    private static String bpfTraceScript;
    @Getter
    private static String remoteLogFile;
    @Getter
    private static String remoteErrorFile;
    @Getter
    private static String localLogFile;
    @Getter
    private static String localErrorFile;
    @Getter
    private static String sshHost;
    @Getter
    private static String sshUser;
    @Getter
    private static String sshPassword;

    static {
        Properties properties = new Properties();
        String propertiesFilePath = getPropertiesFilePath();

        try (InputStream input = Files.newInputStream(Paths.get(propertiesFilePath))) {
            properties.load(input);
            bpfTraceScript = properties.getProperty("bpfTraceScript");
            remoteLogFile = properties.getProperty("remoteLogFile");
            remoteErrorFile = properties.getProperty("remoteErrorFile");
            localLogFile = properties.getProperty("localLogFile");
            localErrorFile = properties.getProperty("localErrorFile");
            sshHost = properties.getProperty("sshHost");
            sshUser = properties.getProperty("sshUser");
            sshPassword = properties.getProperty("sshPassword");
        } catch (IOException ex) {
            System.err.println("Error loading properties file: " + propertiesFilePath);
            ex.printStackTrace();
        }
    }

    private static String getPropertiesFilePath() {
        String workingDir = System.getProperty("user.dir");
        return Paths.get(workingDir, "bpfTrace.properties").toString();
    }
}
