package build.dream.admin.services;

import build.dream.admin.models.application.DeployModel;
import build.dream.common.admin.domains.Application;
import build.dream.common.admin.domains.Server;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.JSchUtils;
import build.dream.common.utils.ValidateUtils;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

@Service
public class ApplicationService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest deploy(DeployModel deployModel) throws JSchException, IOException {
        BigInteger applicationId = deployModel.getApplicationId();
        String type = deployModel.getType();
        String version = deployModel.getVersion();
        BigInteger userId = deployModel.getUserId();

        Application application = DatabaseHelper.find(Application.class, applicationId);
        ValidateUtils.notNull(application, "应用程序不存在！");

        Server server = DatabaseHelper.find(Server.class, application.getServerId());
        ValidateUtils.notNull(server, "服务器不存在！");

        String serviceName = application.getServiceName();
        Session session = JSchUtils.createSession(server.getUserName(), server.getPassword(), server.getIpAddress(), server.getSshPort());
        JSchUtils.executeCommand(session, "sh /usr/local/development/webapps/deployd.sh " + serviceName + " " + type + " " + version);
        JSchUtils.disconnectSession(session);

        application.setType(type);
        application.setVersion(version);
        application.setDeployedTime(new Date());
        application.setDeployedUserId(userId);
        DatabaseHelper.update(application);
        return ApiRest.builder().message("部署成功！").successful(true).build();
    }
}
