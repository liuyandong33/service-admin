package build.dream.devops.utils;

import javax.net.ServerSocketFactory;
import java.net.InetAddress;
import java.util.concurrent.locks.ReentrantLock;

public class NetUtils {
    public static final ReentrantLock REENTRANT_LOCK = new ReentrantLock();
    public static final int PORT_RANGE_MIN = 1024;
    public static final int PORT_RANGE_MAX = 0xFFFF;
    public final static String LOCAL_IP = "127.0.0.1";

    public static boolean isUsableLocalPort(int port) {
        if (!isValidPort(port)) {
            return false;
        }
        try {
            ServerSocketFactory.getDefault().createServerSocket(port, 1, InetAddress.getByName(LOCAL_IP)).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidPort(int port) {
        return port >= 0 && port <= PORT_RANGE_MAX;
    }

    public static int obtainUsableLocalPort() {
        REENTRANT_LOCK.lock();
        try {
            int port = 10000;
            while (!isUsableLocalPort(port)) {
                port++;
            }
            return port;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            REENTRANT_LOCK.unlock();
        }
    }
}
