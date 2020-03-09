package build.dream.devops.services;

import build.dream.devops.models.configuration.AddConfigurationModel;
import build.dream.devops.models.configuration.DeleteConfigurationModel;
import build.dream.devops.models.configuration.ListConfigurationsModel;
import build.dream.devops.models.configuration.UpdateConfigurationModel;
import build.dream.devops.utils.ZookeeperUtils;
import build.dream.common.api.ApiRest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationService {
    /**
     * 新增配置
     *
     * @param addConfigurationModel
     * @return
     * @throws Exception
     */
    public ApiRest addConfiguration(AddConfigurationModel addConfigurationModel) {
        String deploymentEnvironment = addConfigurationModel.getDeploymentEnvironment();
        String partitionCode = addConfigurationModel.getPartitionCode();
        String serviceName = addConfigurationModel.getServiceName();
        String configurationKey = addConfigurationModel.getConfigurationKey();
        String configurationValue = addConfigurationModel.getConfigurationValue();

        ZookeeperUtils.notExistsCreate("/configurations");
        ZookeeperUtils.notExistsCreate("/configurations/" + deploymentEnvironment + "-" + partitionCode + "-" + serviceName);

        String path = "/configurations/" + deploymentEnvironment + "-" + partitionCode + "-" + serviceName + "/" + configurationKey;
        if (ZookeeperUtils.exists(path)) {
            ZookeeperUtils.setData(path, configurationValue);
        } else {
            ZookeeperUtils.create(path, configurationValue);
        }

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

        List<Map<String, String>> configurations = new ArrayList<Map<String, String>>();
        if (ZookeeperUtils.exists(path)) {
            List<String> keys = ZookeeperUtils.getChildren(path);
            for (String key : keys) {
                Map<String, String> configuration = new HashMap<String, String>();
                configuration.put("configurationKey", key);
                configuration.put("configurationValue", ZookeeperUtils.getData(path + "/" + key));
                configurations.add(configuration);
            }
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
        ZookeeperUtils.setData(path, configurationValue);
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
        ZookeeperUtils.delete(path);
        return ApiRest.builder().message("删除配置成功！").successful(true).build();
    }
}
