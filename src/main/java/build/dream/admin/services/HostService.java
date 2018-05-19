package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.host.CreateHostModel;
import build.dream.admin.models.host.ListModel;
import build.dream.admin.models.host.SaveModel;
import build.dream.admin.utils.DatabaseHelper;
import build.dream.common.admin.domains.Host;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.SearchModel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class HostService {
    /**
     * 添加主机
     *
     * @param saveModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest save(SaveModel saveModel) {
        BigInteger id = saveModel.getId();
        BigInteger userId = saveModel.getUserId();
        String name = saveModel.getName();
        String ipAddress = saveModel.getIpAddress();
        int sshPort = saveModel.getSshPort();
        String userName = saveModel.getUserName();
        String password = saveModel.getPassword();

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(userName, ipAddress, sshPort);
            session.setConfig("PreferredAuthentications", "password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect(3000);
            session.disconnect();
        } catch (Exception e) {
            throw new RuntimeException("服务器与宿主机之间无法连通，请检查IP地址、SSH连接端口号、用户名、密码填写是否正确！");
        }

        Host host = null;
        if (id != null) {
            SearchModel searchModel = new SearchModel(true);
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, id);
            host = DatabaseHelper.find(Host.class, searchModel);
            Validate.notNull(host, "主机不存在！");

            host.setName(name);
            host.setIpAddress(ipAddress);
            host.setSshPort(sshPort);
            host.setUserName(userName);
            host.setPassword(password);
            host.setLastUpdateUserId(userId);
            DatabaseHelper.update(host);
        } else {
            host = new Host();
            host.setType(1);
            host.setName(name);
            host.setIpAddress(ipAddress);
            host.setSshPort(sshPort);
            host.setUserName(userName);
            host.setPassword(password);
            host.setCreateUserId(userId);
            host.setLastUpdateUserId(userId);
            DatabaseHelper.insert(host);
        }
        return new ApiRest(host, "保存成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest createHost(CreateHostModel createHostModel) {
        return new ApiRest();
    }

    /**
     * 查询主机列表
     *
     * @param listModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest list(ListModel listModel) {
        return new ApiRest();
    }
}
