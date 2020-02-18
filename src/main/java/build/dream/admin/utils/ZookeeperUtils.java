package build.dream.admin.utils;

import build.dream.admin.constants.Constants;
import build.dream.common.utils.ApplicationHandler;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.DeleteBuilder;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.api.SetDataBuilder;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Objects;

public class ZookeeperUtils {
    private static CuratorFramework curatorFramework;

    private static CuratorFramework obtainCuratorFramework() {
        if (Objects.isNull(curatorFramework)) {
            curatorFramework = ApplicationHandler.getBean(CuratorFramework.class);
        }
        return curatorFramework;
    }

    public static boolean exists(String path) {
        return ApplicationHandler.callMethodSuppressThrow(() -> Objects.nonNull(obtainCuratorFramework().checkExists().forPath(path)));
    }

    public static boolean notExists(String path) {
        return !exists(path);
    }

    public static String create(String path, String value) {
        return ApplicationHandler.callMethodSuppressThrow(() -> obtainCuratorFramework().create().withMode(CreateMode.PERSISTENT).forPath(path, value.getBytes(Constants.CHARSET_UTF_8)));
    }

    public static String create(String path) {
        return create(path, "");
    }

    public static void notExistsCreate(String path, String value) {
        if (notExists(path)) {
            create(path, value);
        }
    }

    public static void notExistsCreate(String path) {
        notExistsCreate(path, "");
    }

    public static Stat setData(String path, String value) {
        return ApplicationHandler.callMethodSuppressThrow(() -> {
            SetDataBuilder setDataBuilder = obtainCuratorFramework().setData();
            return setDataBuilder.forPath(path, value.getBytes(Constants.CHARSET_UTF_8));
        });
    }

    public static List<String> getChildren(String path) {
        return ApplicationHandler.callMethodSuppressThrow(() -> {
            GetChildrenBuilder getChildrenBuilder = obtainCuratorFramework().getChildren();
            return getChildrenBuilder.forPath(path);
        });
    }

    public static String getData(String path) {
        return ApplicationHandler.callMethodSuppressThrow(() -> {
            GetDataBuilder getDataBuilder = obtainCuratorFramework().getData();
            return new String(getDataBuilder.forPath(path), Constants.CHARSET_UTF_8);
        });
    }

    public static void delete(String path) {
        ApplicationHandler.callMethodSuppressThrow(() -> {
            DeleteBuilder deleteBuilder = obtainCuratorFramework().delete();
            deleteBuilder.forPath(path);
        });
    }
}
