package build.dream.admin.utils;

import build.dream.admin.constants.Constants;
import build.dream.common.utils.ApplicationHandler;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ZooKeeperUtils {
    private static ZooKeeper zooKeeper = null;

    public static ZooKeeper obtainZooKeeper() {
        if (zooKeeper == null) {
            zooKeeper = ApplicationHandler.getBean(ZooKeeper.class);
        }
        return zooKeeper;
    }

    public static String create(String path, byte data[], List<ACL> acl, CreateMode createMode) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        String[] splits = path.substring(1).split("/");
        String parentPath = "";
        ZooKeeper zooKeeper = obtainZooKeeper();
        for (String split : splits) {
            parentPath = parentPath + "/" + split;
            if (parentPath.equals(path)) {
                break;
            }
            if (zooKeeper.exists(parentPath, false) == null) {
                zooKeeper.create(parentPath, "".getBytes(Constants.CHARSET_NAME_UTF_8), acl, createMode);
            }
        }
        return zooKeeper.create(path, data, acl, createMode);
    }
}
