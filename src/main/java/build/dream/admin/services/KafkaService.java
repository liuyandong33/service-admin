package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.kafka.*;
import build.dream.admin.utils.DatabaseHelper;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.admin.domains.KafkaNode;
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
public class KafkaService {
    /**
     * 获取zookeeper 节点
     *
     * @param listNodesModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listNodes(ListNodesModel listNodesModel) {
        BigInteger clusterId = listNodesModel.getClusterId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("cluster_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, clusterId);
        List<KafkaNode> kafkaNodes = DatabaseHelper.findAll(KafkaNode.class, searchModel);
        return new ApiRest(kafkaNodes, "获取 Kafka 节点成功！");
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

        KafkaNode kafkaNode = DatabaseHelper.find(KafkaNode.class, nodeId);
        Validate.notNull(kafkaNode, "Kafka 节点不存在！");

        Session session = JSchUtils.createSession(kafkaNode.getUserName(), kafkaNode.getPassword(), kafkaNode.getIpAddress(), kafkaNode.getSshPort());

        String startCommand = kafkaNode.getKafkaHome() + "/bin/zkServer.sh start";
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

        KafkaNode kafkaNode = DatabaseHelper.find(KafkaNode.class, nodeId);
        Validate.notNull(kafkaNode, "Kafka 节点不存在！");

        Session session = JSchUtils.createSession(kafkaNode.getUserName(), kafkaNode.getPassword(), kafkaNode.getIpAddress(), kafkaNode.getSshPort());

        String stopCommand = kafkaNode.getKafkaHome() + "/bin/zkServer.sh stop";
        String stopResult = JSchUtils.executeCommand(session, stopCommand);

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest(stopResult);
        apiRest.setMessage("停止 Kafka 节点成功！");
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

        KafkaNode kafkaNode = DatabaseHelper.find(KafkaNode.class, nodeId);
        Validate.notNull(kafkaNode, "Kafka 节点不存在！");

        Session session = JSchUtils.createSession(kafkaNode.getUserName(), kafkaNode.getPassword(), kafkaNode.getIpAddress(), kafkaNode.getSshPort());

        String restartCommand = kafkaNode.getKafkaHome() + "/bin/zkServer.sh stop";
        String restartResult = JSchUtils.executeCommand(session, restartCommand);

        JSchUtils.disconnectSession(session);

        return new ApiRest(restartResult, "重启 Kafka 节点成功！");
    }

    /**
     * 获取 Zookeeper 节点状态
     *
     * @param statusModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    public ApiRest status(StatusModel statusModel) throws JSchException, IOException {
        BigInteger nodeId = statusModel.getNodeId();

        KafkaNode kafkaNode = DatabaseHelper.find(KafkaNode.class, nodeId);
        Validate.notNull(kafkaNode, "Kafka 节点不存在！");

        Session session = JSchUtils.createSession(kafkaNode.getUserName(), kafkaNode.getPassword(), kafkaNode.getIpAddress(), kafkaNode.getSshPort());

        String statusCommand = kafkaNode.getKafkaHome() + "/bin/zkServer.sh status";
        String statusResult = JSchUtils.executeCommand(session, statusCommand);

        JSchUtils.disconnectSession(session);

        return new ApiRest(statusResult, "获取 Kafka 节点状态成功！");
    }

    /**
     * 保存 Zookeeper 节点
     *
     * @param saveNodeModel
     * @return
     */
    public ApiRest saveNode(SaveNodeModel saveNodeModel) {
        BigInteger id = saveNodeModel.getId();
        String hostName = saveNodeModel.getHostName();
        String ipAddress = saveNodeModel.getIpAddress();
        Integer sshPort = saveNodeModel.getSshPort();
        String userName = saveNodeModel.getUserName();
        String password = saveNodeModel.getPassword();
        String kafkaHome = saveNodeModel.getKafkaHome();
        BigInteger userId = saveNodeModel.getUserId();

        try {
            Session session = JSchUtils.createSession(userName, password, ipAddress, sshPort);
            JSchUtils.disconnectSession(session);
        } catch (Exception e) {
            throw new RuntimeException("服务器与宿主机之间无法连通，请检查IP地址、SSH连接端口号、用户名、密码填写是否正确！");
        }

        KafkaNode kafkaNode = null;
        if (id != null) {
            kafkaNode = DatabaseHelper.find(KafkaNode.class, id);
            Validate.notNull(kafkaNode, "Kafka 节点不存在！");

            kafkaNode.setHostName(hostName);
            kafkaNode.setIpAddress(ipAddress);
            kafkaNode.setSshPort(sshPort);
            kafkaNode.setUserName(userName);
            kafkaNode.setPassword(password);
            kafkaNode.setKafkaHome(kafkaHome);
            kafkaNode.setLastUpdateUserId(userId);
            DatabaseHelper.update(kafkaNode);
        } else {
            kafkaNode = new KafkaNode();
            kafkaNode.setHostName(hostName);
            kafkaNode.setIpAddress(ipAddress);
            kafkaNode.setSshPort(sshPort);
            kafkaNode.setUserName(userName);
            kafkaNode.setPassword(password);
            kafkaNode.setKafkaHome(kafkaHome);
            DatabaseHelper.insert(kafkaHome);
        }

        return new ApiRest(kafkaNode, "保存 Kafka 节点失败！");
    }
}
