package build.dream.admin.services;

import build.dream.admin.models.application.DeployModel;
import build.dream.admin.models.application.StopModel;
import build.dream.common.api.ApiRest;
import build.dream.common.domains.admin.App;
import build.dream.common.domains.admin.AppConfiguration;
import build.dream.common.domains.admin.Host;
import build.dream.common.domains.admin.JavaOperation;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import com.jcraft.jsch.JSchException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class AppService {
    /**
     * 部署应用程序
     *
     * @param deployModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest deploy(DeployModel deployModel) throws JSchException, IOException {
        Long appId = deployModel.getAppId();
        String type = deployModel.getType();
        String version = deployModel.getVersion();

        App app = DatabaseHelper.find(App.class, appId);
        ValidateUtils.notNull(app, "应用程序不存在！");

        Host host = DatabaseHelper.find(Host.class, app.getHostId());
        ValidateUtils.notNull(host, "服务器不存在！");

        SearchModel javaOperationSearchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(JavaOperation.ColumnName.APP_ID, appId)
                .build();
        JavaOperation javaOperation = DatabaseHelper.find(JavaOperation.class, javaOperationSearchModel);

        SearchModel appConfigurationSearchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(AppConfiguration.ColumnName.APP_ID, appId)
                .build();
        List<AppConfiguration> appConfigurations = DatabaseHelper.findAll(AppConfiguration.class, appConfigurationSearchModel);

        StringBuilder command = new StringBuilder("nohup java");
        if (Objects.nonNull(javaOperation)) {
            command.append(" ");
            command.append(javaOperation.buildJavaOpts());
        }

        command.append(" -jar");

        for (AppConfiguration appConfiguration : appConfigurations) {
            command.append(" ");
            command.append("--");
            command.append(appConfiguration.getConfigurationKey());
            command.append("=");
            command.append(appConfiguration.getConfigurationValue());
        }

        command.append(" &");

        return ApiRest.builder().message("部署成功！").successful(true).build();
    }

    /**
     * 停止应用程序
     *
     * @param stopModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest stop(StopModel stopModel) throws JSchException, IOException {
        Long appId = stopModel.getAppId();
        App app = DatabaseHelper.find(App.class, appId);
        ValidateUtils.notNull(app, "应用程序不存在！");

        Host host = DatabaseHelper.find(Host.class, app.getHostId());
        ValidateUtils.notNull(host, "服务器不存在！");

        return ApiRest.builder().message("停止应用成功！").successful(true).build();
    }
}
