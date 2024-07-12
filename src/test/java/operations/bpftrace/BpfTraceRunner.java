package operations.bpftrace;

import com.jcraft.jsch.*;

import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class BpfTraceRunner {
    private Process bpfTraceProcess;
    private Session sshSession;
    private ScheduledExecutorService scheduler;

    public void startBpfTrace() {
        if (BpfTraceConf.isUseSSH()) {
            startBpfTraceRemote();
            startLogFileUpdateTask();
        } else {
            startBpfTraceLocal();
        }
    }

    private void startBpfTraceLocal() {
        ProcessBuilder builder = new ProcessBuilder("sudo", "bpftrace", "-f", "json", BpfTraceConf.getBpfTraceScript());
        builder.redirectOutput(ProcessBuilder.Redirect.to(new File(BpfTraceConf.getLocalLogFile())));
        builder.redirectError(ProcessBuilder.Redirect.to(new File(BpfTraceConf.getLocalErrorFile())));
        try {
            bpfTraceProcess = builder.start();
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Register shutdown hook to ensure the process is destroyed
        Runtime.getRuntime().addShutdownHook(new Thread(this::stopBpfTrace));
    }

    private void startBpfTraceRemote() {
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(BpfTraceConf.getSshUser(), BpfTraceConf.getSshHost(), 22);
            sshSession.setPassword(BpfTraceConf.getSshPassword());

            // Avoid asking for key confirmation
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(config);

            sshSession.connect();

            String command = "sudo bpftrace " + BpfTraceConf.getBpfTraceScript() + " > " + BpfTraceConf.getRemoteLogFile() + " 2> " + BpfTraceConf.getRemoteErrorFile();

            ChannelExec channelExec = (ChannelExec) sshSession.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();
            Thread.sleep(5000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stopBpfTrace() {
        if (BpfTraceConf.isUseSSH()) {
            if (sshSession != null && sshSession.isConnected()) {
                sshSession.disconnect();
            }
        } else {
            if (bpfTraceProcess != null) {
                ProcessHandle processHandle = bpfTraceProcess.toHandle();
                try {
                    ProcessBuilder builder = new ProcessBuilder("sudo", "kill", "-SIGINT", Long.toString(processHandle.pid()));
                    builder.inheritIO();
                    Process killProcess = builder.start();
                    killProcess.waitFor();
                    Thread.sleep(3000);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
        if (BpfTraceConf.isUseSSH()) {
            if (sshSession == null || !sshSession.isConnected()) {
                throw new IllegalStateException("SSH session is not connected.");
            }

            try {
                ChannelSftp sftpChannel = (ChannelSftp) sshSession.openChannel("sftp");
                sftpChannel.connect();

                sftpChannel.get(BpfTraceConf.getRemoteLogFile(), BpfTraceConf.getLocalLogFile());
                sftpChannel.get(BpfTraceConf.getRemoteErrorFile(), BpfTraceConf.getLocalErrorFile());

                sftpChannel.disconnect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
