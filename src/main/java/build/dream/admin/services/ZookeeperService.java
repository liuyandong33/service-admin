package build.dream.admin.services;

import build.dream.admin.models.zookeeper.ListNodesModel;
import build.dream.admin.models.zookeeper.StartModel;
import build.dream.admin.models.zookeeper.StopModel;
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
        apiRest.setMessage("Zookeeper 启动成功！");
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
        String startResult = JSchUtils.executeCommand(session, stopCommand);

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest(startResult);
        apiRest.setMessage("Zookeeper 停止成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
