package operations.testplan;

import java.io.IOException;

public class BpfTraceRunner {

    private Process bpfTraceProcess;
    private String bpfTraceScript = "trace_pg_exec_functions.bt";
    private String logFile = "bg.log";
    private String errorFile = "error.log";

    public void startBpfTrace() {
        ProcessBuilder builder = new ProcessBuilder("sudo", "bpftrace", bpfTraceScript);
        builder.redirectOutput(ProcessBuilder.Redirect.to(new java.io.File(logFile)));
        builder.redirectError(ProcessBuilder.Redirect.to(new java.io.File(errorFile)));
        try {
            bpfTraceProcess = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopBpfTrace() {
        if (bpfTraceProcess != null) {
            bpfTraceProcess.destroy();
        }
    }
}
