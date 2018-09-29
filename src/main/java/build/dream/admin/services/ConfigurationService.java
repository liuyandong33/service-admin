package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.configuration.AddConfigurationModel;
import build.dream.admin.models.configuration.DeleteConfigurationModel;
import build.dream.admin.models.configuration.ListConfigurationsModel;
import build.dream.admin.models.configuration.UpdateConfigurationModel;
import build.dream.common.api.ApiRest;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.*;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationService {
    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 新增配置
     *
     * @param addConfigurationModel
     * @return
     * @throws Exception
     */
    public ApiRest addConfiguration(AddConfigurationModel addConfigurationModel) throws Exception {
        String deploymentEnvironment = addConfigurationModel.getDeploymentEnvironment();
        String partitionCode = addConfigurationModel.getPartitionCode();
        String serviceName = addConfigurationModel.getServiceName();
        String configurationKey = addConfigurationModel.getConfigurationKey();
        String configurationValue = addConfigurationModel.getConfigurationValue();

        String path = "/configurations/" + deploymentEnvironment + "-" + partitionCode + "-" + serviceName + "/" + configurationKey;
        CreateBuilder createBuilder = curatorFramework.create();
        createBuilder.withMode(CreateMode.PERSISTENT).forPath(path, configurationValue.getBytes(Constants.CHARSET_NAME_UTF_8));

        return ApiRest.builder().message("新增配置成功！").successful(true).build();
    }

    /**
     * 查询配置
     *
     * @param listConfigurationsModel
     * @return
     * @throws Exception
     */
    public ApiRest listConfigurations(ListConfigurationsModel listConfigurationsModel) throws Exception {
        String deploymentEnvironment = listConfigurationsModel.getDeploymentEnvironment();
        String partitionCode = listConfigurationsModel.getPartitionCode();
        String serviceName = listConfigurationsModel.getServiceName();

        String path = "/configurations/" + deploymentEnvironment + "-" + partitionCode + "-" + serviceName;
        GetChildrenBuilder getChildrenBuilder = curatorFramework.getChildren();
        List<String> keys = getChildrenBuilder.forPath(path);

        GetDataBuilder getDataBuilder = curatorFramework.getData();

        List<Map<String, String>> configurations = new ArrayList<Map<String, String>>();
        for (String key : keys) {
            String configurationValue = new String(getDataBuilder.forPath(path + "/" + key), Constants.CHARSET_NAME_UTF_8);
            Map<String, String> configuration = new HashMap<String, String>();
            configuration.put("configurationKey", key);

            configuration.put("configurationValue", configurationValue);
            configurations.add(configuration);
        }

        return ApiRest.builder().data(configurations).message("查询配置成功！").successful(true).build();
    }

    /**
     * 修改配置
     *
     * @param updateConfigurationModel
     * @return
     * @throws Exception
     */
    public ApiRest updateConfiguration(UpdateConfigurationModel updateConfigurationModel) throws Exception {
        String deploymentEnvironment = updateConfigurationModel.getDeploymentEnvironment();
        String partitionCode = updateConfigurationModel.getPartitionCode();
        String serviceName = updateConfigurationModel.getServiceName();
        String configurationKey = updateConfigurationModel.getConfigurationKey();
        String configurationValue = updateConfigurationModel.getConfigurationValue();

        String path = "/configurations/" + deploymentEnvironment + "-" + partitionCode + "-" + serviceName + "/" + configurationKey;

        SetDataBuilder setDataBuilder = curatorFramework.setData();
        setDataBuilder.forPath(path, configurationValue.getBytes(Constants.CHARSET_NAME_UTF_8));
        return ApiRest.builder().message("修改配置成功！").successful(true).build();
    }

    /**
     * 删除配置
     *
     * @param deleteConfigurationModel
     * @return
     * @throws Exception
     */
    public ApiRest deleteConfiguration(DeleteConfigurationModel deleteConfigurationModel) throws Exception {
        String deploymentEnvironment = deleteConfigurationModel.getDeploymentEnvironment();
        String partitionCode = deleteConfigurationModel.getPartitionCode();
        String serviceName = deleteConfigurationModel.getServiceName();
        String configurationKey = deleteConfigurationModel.getConfigurationKey();

        String path = "/configurations/" + deploymentEnvironment + "-" + partitionCode + "-" + serviceName + "/" + configurationKey;

        DeleteBuilder deleteBuilder = curatorFramework.delete();
        deleteBuilder.forPath(path);
        return ApiRest.builder().message("删除配置成功！").successful(true).build();
    }
}
