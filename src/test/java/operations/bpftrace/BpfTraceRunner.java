package operations.bpftrace;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BpfTraceRunner {

    private Process bpfTraceProcess;
    private final String bpfTraceScript;
    private final String remoteLogFile;
    private final String remoteErrorFile;
    private final String localLogFile;
    private final String localErrorFile;
    private final String sshHost;
    private final String sshUser;
    private final String sshPassword;
    private Session sshSession;
    private final boolean useSSH;
    private ScheduledExecutorService scheduler;

    public BpfTraceRunner() {
        bpfTraceScript = BpfTraceConf.getBpfTraceScript();
        remoteLogFile = BpfTraceConf.getRemoteLogFile();
        remoteErrorFile = BpfTraceConf.getRemoteErrorFile();
        localLogFile = BpfTraceConf.getLocalLogFile();
        localErrorFile = BpfTraceConf.getLocalErrorFile();
        sshHost = BpfTraceConf.getSshHost();
        sshUser = BpfTraceConf.getSshUser();
        sshPassword = BpfTraceConf.getSshPassword();
        useSSH  = sshHost != null && !sshHost.isEmpty() && sshUser != null && !sshUser.isEmpty();
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
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
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
            Thread.sleep(5000);
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


