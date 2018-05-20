package build.dream.admin.services;

import build.dream.admin.models.zookeeper.ListNodesModel;
import build.dream.admin.models.zookeeper.StartModel;
import build.dream.admin.models.zookeeper.StopModel;
import build.dream.admin.utils.DatabaseHelper;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.admin.domains.ZookeeperNode;
import build.dream.common.api.ApiRest;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;

@Service
public class ZookeeperService {
    @Transactional(readOnly = true)
    public ApiRest listNodes(ListNodesModel listNodesModel) {
        return new ApiRest();
    }

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
        apiRest.setMessage("Zookeeper 启动成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    @Transactional(readOnly = true)
    public ApiRest stop(StopModel stopModel) throws JSchException, IOException {
        BigInteger nodeId = stopModel.getNodeId();

        ZookeeperNode zookeeperNode = DatabaseHelper.find(ZookeeperNode.class, nodeId);
        Validate.notNull(zookeeperNode, "Zookeeper 节点不存在！");

        Session session = JSchUtils.createSession(zookeeperNode.getUserName(), zookeeperNode.getPassword(), zookeeperNode.getIpAddress(), zookeeperNode.getSshPort());

        String stopCommand = zookeeperNode.getZookeeperHome() + "/bin/zkServer.sh stop";
        String startResult = JSchUtils.executeCommand(session, stopCommand);

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest(startResult);
        apiRest.setMessage("Zookeeper 停止成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
