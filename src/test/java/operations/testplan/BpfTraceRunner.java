package operations.testplan;

import com.jcraft.jsch.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BpfTraceRunner {

    private Process bpfTraceProcess;
    private String bpfTraceScript;
    private String remoteLogFile;
    private String remoteErrorFile;
    private String localLogFile;
    private String localErrorFile;
    private String sshHost;
    private String sshUser;
    private String sshPassword;
    private Session sshSession;
    private boolean useSSH;
    private ScheduledExecutorService scheduler;
    private static final String path = "bpfTrace.properties";

    public BpfTraceRunner() {
        Properties properties = new Properties();
        String propertiesFilePath = getPropertiesFilePath(path);

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

            // Определить, нужно ли использовать SSH
            useSSH = sshHost != null && !sshHost.isEmpty() && sshUser != null && !sshUser.isEmpty();

        } catch (IOException ex) {
            System.err.println("Error loading properties file: " + propertiesFilePath);
            ex.printStackTrace();
        }
    }

    private String getPropertiesFilePath(String fileName) {
        String workingDir = System.getProperty("user.dir");
        return Paths.get(workingDir, fileName).toString();
    }

    public void startBpfTrace() {
        if (useSSH) {
            startBpfTraceRemote();
            startLogFileUpdateTask();
        } else {
            startBpfTraceLocal();
        }
    }

    private void startBpfTraceLocal() {
        ProcessBuilder builder = new ProcessBuilder("sudo", "bpftrace", bpfTraceScript);
        builder.redirectOutput(ProcessBuilder.Redirect.to(new File(localLogFile)));
        builder.redirectError(ProcessBuilder.Redirect.to(new File(localErrorFile)));
        try {
            bpfTraceProcess = builder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void startBpfTraceRemote() {
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(sshUser, sshHost, 22);
            sshSession.setPassword(sshPassword);

            // Avoid asking for key confirmation
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(config);

            sshSession.connect();

            String command = "sudo bpftrace " + bpfTraceScript + " > " + remoteLogFile + " 2> " + remoteErrorFile;

            ChannelExec channelExec = (ChannelExec) sshSession.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopBpfTrace() {
        if (useSSH) {
            if (sshSession != null && sshSession.isConnected()) {
                sshSession.disconnect();
            }
        } else {
            if (bpfTraceProcess != null) {
                bpfTraceProcess.destroy();
            }
        }
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private void startLogFileUpdateTask() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::copyLogsToLocal, 0, 1, TimeUnit.MINUTES);
    }

    private void copyLogsToLocal() {
        if (useSSH) {
            if (sshSession == null || !sshSession.isConnected()) {
                throw new IllegalStateException("SSH session is not connected.");
            }

            try {
                ChannelSftp sftpChannel = (ChannelSftp) sshSession.openChannel("sftp");
                sftpChannel.connect();

                sftpChannel.get(remoteLogFile, localLogFile);
                sftpChannel.get(remoteErrorFile, localErrorFile);

                sftpChannel.disconnect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
