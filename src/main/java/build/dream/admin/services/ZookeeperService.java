package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.zookeeper.*;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.api.ApiRest;
import build.dream.common.domains.admin.ZookeeperNode;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
        Long clusterId = listNodesModel.getClusterId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("cluster_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, clusterId);
        List<ZookeeperNode> zookeeperNodes = DatabaseHelper.findAll(ZookeeperNode.class, searchModel);
        return new ApiRest(zookeeperNodes, "获取Zookeeper节点成功！");
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
        Long nodeId = startModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        ValidateUtils.notNull(zookeeperNode, "Zookeeper 节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String startCommand = "sh " + zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh start";
        String startResult = JSchUtils.executeCommand(session, startCommand);

        JSchUtils.disconnectSession(session);

        return ApiRest.builder().data(startResult).message("启动Zookeeper节点成功！").successful(true).build();
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
        Long nodeId = stopModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        ValidateUtils.notNull(zookeeperNode, "Zookeeper 节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String stopCommand = "sh " + zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh stop";
        String stopResult = JSchUtils.executeCommand(session, stopCommand);

        JSchUtils.disconnectSession(session);

        return ApiRest.builder().data(stopResult).message("停止Zookeeper节点成功！").successful(true).build();
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
        Long nodeId = restartModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        ValidateUtils.notNull(zookeeperNode, "Zookeeper节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String restartCommand = "sh " + zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh restart";
        String restartResult = JSchUtils.executeCommand(session, restartCommand);

        JSchUtils.disconnectSession(session);

        return ApiRest.builder().data(restartResult).message("重启Zookeeper节点成功！").successful(true).build();
    }

    /**
     * 获取 Zookeeper 节点状态
     *
     * @param statusModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public ApiRest status(StatusModel statusModel) throws JSchException, IOException {
        Long nodeId = statusModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        ValidateUtils.notNull(zookeeperNode, "Zookeeper 节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String statusCommand = "sh " + zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh status";
        String statusResult = JSchUtils.executeCommand(session, statusCommand);

        JSchUtils.disconnectSession(session);

        return ApiRest.builder().data(statusResult).message("获取Zookeeper节点状态成功！").successful(true).build();
    }

    /**
     * 保存 Zookeeper 节点
     *
     * @param saveNodeModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveNode(SaveNodeModel saveNodeModel) {
        Long id = saveNodeModel.getId();
        String hostName = saveNodeModel.getHostName();
        String ipAddress = saveNodeModel.getIpAddress();
        Integer sshPort = saveNodeModel.getSshPort();
        String userName = saveNodeModel.getUserName();
        String password = saveNodeModel.getPassword();
        String zookeeperHome = saveNodeModel.getZookeeperHome();
        Long userId = saveNodeModel.getUserId();

        try {
            Session session = JSchUtils.createSession(userName, password, ipAddress, sshPort);
            JSchUtils.disconnectSession(session);
        } catch (Exception e) {
            throw new RuntimeException("服务器与宿主机之间无法连通，请检查IP地址、SSH连接端口号、用户名、密码填写是否正确！");
        }

        ZookeeperNode zookeeperNode = null;
        if (id != null) {
            zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, id);
            ValidateUtils.notNull(zookeeperNode, "Zookeeper节点不存在！");

            zookeeperNode.setHostName(hostName);
            zookeeperNode.setIpAddress(ipAddress);
            zookeeperNode.setSshPort(sshPort);
            zookeeperNode.setUserName(userName);
            zookeeperNode.setPassword(password);
            zookeeperNode.setZookeeperHome(zookeeperHome);
            zookeeperNode.setUpdatedUserId(userId);
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

        return ApiRest.builder().data(zookeeperNode).message("保存Zookeeper节点失败！").successful(true).build();
    }
}
