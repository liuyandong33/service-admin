package build.dream.devops.utils;

import build.dream.devops.constants.Constants;
import build.dream.common.utils.ValidateUtils;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class JSchUtils {
    public static Session createSession(String userName, String password, String ipAddress, int port) throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(userName, ipAddress, port);
        session.setConfig("PreferredAuthentications", "password");
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(password);
        session.connect(30000);
        return session;
    }

    public static void disconnectSession(Session session) {
        if (session != null) {
            session.disconnect();
        }
    }

    public static Channel openChannel(Session session, String type) throws JSchException {
        return session.openChannel(type);
    }

    public static void disconnectChannel(Channel channel) {
        if (channel != null) {
            channel.disconnect();
        }
    }

    public static String executeCommand(Session session, String command) {
        ChannelExec channelExec = null;
        String result = null;
        try {
            channelExec = (ChannelExec) openChannel(session, Constants.CHANNEL_TYPE_EXEC);
            channelExec.setCommand(command);
            channelExec.connect();
            InputStream inputStream = channelExec.getInputStream();
            result = IOUtils.toString(inputStream, Constants.CHARSET_UTF_8);

            int exitStatus = channelExec.getExitStatus();
            ValidateUtils.isTrue(exitStatus == 0, result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            disconnectChannel(channelExec);
        }
        return result;
    }

    public static boolean exists(Session session, String path) {
        return "yes".equals(executeCommand(session, "[ -e " + path + " ] && echo -e yes'\\c' || echo -e no'\\c'"));
    }

    public static void mkdir(Session session, String path) {
        executeCommand(session, "mkdir " + path);
    }

    public static void mkdirs(Session session, String path) {
        executeCommand(session, "mkdir -p " + path);
    }

    public static void delete(Session session, String path) {
        executeCommand(session, "rm -rf " + path);
    }
}
