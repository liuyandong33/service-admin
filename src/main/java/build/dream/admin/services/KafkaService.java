package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.kafka.ListNodesModel;
import build.dream.admin.models.kafka.SaveNodeModel;
import build.dream.admin.models.kafka.StartModel;
import build.dream.admin.models.kafka.StopModel;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.admin.domains.KafkaNode;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
public class KafkaService {
    /**
     * 获取kafka节点
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
        return ApiRest.builder().data(kafkaNodes).message("获取Kafka节点成功").successful(true).build();
    }

    /**
     * 启动kafka节点
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
        ValidateUtils.notNull(kafkaNode, "Kafka节点不存在！");

        Session session = JSchUtils.createSession(kafkaNode.getUserName(), kafkaNode.getPassword(), kafkaNode.getIpAddress(), kafkaNode.getSshPort());

        String startCommand = "sh " + kafkaNode.getKafkaHome() + "/bin/kafka-server-start.sh";
        String startResult = JSchUtils.executeCommand(session, startCommand);

        JSchUtils.disconnectSession(session);

        return ApiRest.builder().data(startResult).message("启动Kafka节点成功！").successful(true).build();
    }

    /**
     * 停止kafka节点
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
        ValidateUtils.notNull(kafkaNode, "Kafka节点不存在！");

        Session session = JSchUtils.createSession(kafkaNode.getUserName(), kafkaNode.getPassword(), kafkaNode.getIpAddress(), kafkaNode.getSshPort());

        String stopCommand = "sh " + kafkaNode.getKafkaHome() + "/bin/kafka-server-stop.sh";
        String stopResult = JSchUtils.executeCommand(session, stopCommand);

        JSchUtils.disconnectSession(session);

        return ApiRest.builder().data(stopResult).message("停止Kafka节点成功！").successful(true).build();
    }

    /**
     * 保存kafka节点
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
            ValidateUtils.notNull(kafkaNode, "Kafka节点不存在！");

            kafkaNode.setHostName(hostName);
            kafkaNode.setIpAddress(ipAddress);
            kafkaNode.setSshPort(sshPort);
            kafkaNode.setUserName(userName);
            kafkaNode.setPassword(password);
            kafkaNode.setKafkaHome(kafkaHome);
            kafkaNode.setUpdatedUserId(userId);
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

        return ApiRest.builder().data(kafkaNode).message("保存Kafka节点失败！").successful(true).build();
    }
}
