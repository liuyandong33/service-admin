package build.dream.devops.utils;

import java.io.IOException;
import java.net.InetAddress;

public class HostUtils {
    public static boolean ping(String host) throws IOException {
        return InetAddress.getByName(host).isReachable(3000);
    }
}
