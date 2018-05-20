package build.dream.admin.services;

import build.dream.admin.models.zookeeper.*;
import build.dream.admin.utils.DatabaseHelper;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.admin.domains.ZookeeperNode;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.SearchModel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
public class ZookeeperService {
    /**
     * 获取zookeeper 节点
     *
     * @param listNodesModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listNodes(ListNodesModel listNodesModel) {
        SearchModel searchModel = new SearchModel(true);
        List<ZookeeperNode> zookeeperNodes = DatabaseHelper.findAll(ZookeeperNode.class, searchModel);
        return new ApiRest(zookeeperNodes, "获取 Zookeeper 节点成功！");
    }

    /**
     * 启动 zookeeper 节点
     *
     * @param startModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public ApiRest start(StartModel startModel) throws JSchException, IOException {
        BigInteger nodeId = startModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        Validate.notNull(zookeeperNode, "Zookeeper 节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String startCommand = zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh start";
        String startResult = JSchUtils.executeCommand(session, startCommand);

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest(startResult);
        apiRest.setMessage("启动 Zookeeper 节点成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 停止 zookeeper 节点
     *
     * @param stopModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public ApiRest stop(StopModel stopModel) throws JSchException, IOException {
        BigInteger nodeId = stopModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        Validate.notNull(zookeeperNode, "Zookeeper 节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String stopCommand = zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh stop";
        String stopResult = JSchUtils.executeCommand(session, stopCommand);

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest(stopResult);
        apiRest.setMessage("停止 Zookeeper 节点成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 停止 zookeeper 节点
     *
     * @param restartModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public ApiRest restart(RestartModel restartModel) throws JSchException, IOException {
        BigInteger nodeId = restartModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        Validate.notNull(zookeeperNode, "Zookeeper 节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String restartCommand = zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh stop";
        String restartResult = JSchUtils.executeCommand(session, restartCommand);

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest(restartResult);
        apiRest.setMessage("重启 Zookeeper 节点成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    public ApiRest saveNode(SaveNodeModel saveNodeModel) {
        BigInteger id = saveNodeModel.getId();
        String hostName = saveNodeModel.getHostName();
        String ipAddress = saveNodeModel.getIpAddress();
        Integer sshPort = saveNodeModel.getSshPort();
        String userName = saveNodeModel.getUserName();
        String password = saveNodeModel.getPassword();
        String zookeeperHome = saveNodeModel.getZookeeperHome();
        BigInteger userId = saveNodeModel.getUserId();

        try {
            Session session = JSchUtils.createSession(userName, password, ipAddress, sshPort);
            JSchUtils.disconnectSession(session);
        } catch (Exception e) {
            throw new RuntimeException("服务器与宿主机之间无法连通，请检查IP地址、SSH连接端口号、用户名、密码填写是否正确！");
        }

        ZookeeperNode zookeeperNode = null;
        if (id != null) {
            zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, id);
            Validate.notNull(zookeeperNode, "Zookeeper 节点不存在！");

            zookeeperNode.setHostName(hostName);
            zookeeperNode.setIpAddress(ipAddress);
            zookeeperNode.setSshPort(sshPort);
            zookeeperNode.setUserName(userName);
            zookeeperNode.setPassword(password);
            zookeeperNode.setZookeeperHome(zookeeperHome);
            zookeeperNode.setLastUpdateUserId(userId);
            DatabaseHelper.update(zookeeperNode);
        } else {
            zookeeperNode = new ZookeeperNode();
            zookeeperNode.setHostName(hostName);
            zookeeperNode.setIpAddress(ipAddress);
            zookeeperNode.setSshPort(sshPort);
            zookeeperNode.setUserName(userName);
            zookeeperNode.setPassword(password);
            zookeeperNode.setZookeeperHome(zookeeperHome);
            DatabaseHelper.insert(zookeeperNode);
        }

        return new ApiRest(zookeeperNode, "保存 Zookeeper 节点失败！");
    }
}
