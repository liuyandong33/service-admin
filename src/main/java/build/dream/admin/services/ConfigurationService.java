package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.configuration.AddConfigurationModel;
import build.dream.admin.models.configuration.DeleteConfigurationModel;
import build.dream.admin.models.configuration.ListConfigurationsModel;
import build.dream.admin.models.configuration.UpdateConfigurationModel;
import build.dream.admin.utils.ZooKeeperUtils;
import build.dream.common.api.ApiRest;
import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigurationService {
    @Autowired
    private ZooKeeper zooKeeper;

    /**
     * 新增配置
     *
     * @param addConfigurationModel
     * @return
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     * @throws InterruptedException
     */
    public ApiRest addConfiguration(AddConfigurationModel addConfigurationModel) throws UnsupportedEncodingException, KeeperException, InterruptedException {
        String path = "/configurations/" + addConfigurationModel.getDeploymentEnvironment() + "-" + addConfigurationModel.getPartitionCode() + "-" + addConfigurationModel.getServiceName() + "/" + addConfigurationModel.getConfigurationKey();
        ZooKeeperUtils.create(path, addConfigurationModel.getConfigurationValue().getBytes(Constants.CHARSET_NAME_UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("新增配置成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 查询配置
     *
     * @param listConfigurationsModel
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     */
    public ApiRest listConfigurations(ListConfigurationsModel listConfigurationsModel) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        String path = "/configurations/" + listConfigurationsModel.getDeploymentEnvironment() + "-" + listConfigurationsModel.getPartitionCode() + "-" + listConfigurationsModel.getServiceName();
        List<String> children = zooKeeper.getChildren(path, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

            }
        });

        List<Map<String, String>> configurations = new ArrayList<Map<String, String>>();
        for (String child : children) {
            Map<String, String> configuration = new HashMap<String, String>();
            configuration.put("configurationKey", child);

            String configurationValue = new String(zooKeeper.getData(path + "/" + child, false, null), Constants.CHARSET_NAME_UTF_8);
            configuration.put("configurationValue", configurationValue);
            configurations.add(configuration);
        }

        ApiRest apiRest = new ApiRest();
        apiRest.setData(configurations);
        apiRest.setMessage("查询配置成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 修改配置
     *
     * @param updateConfigurationModel
     * @return
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     * @throws InterruptedException
     */
    public ApiRest updateConfiguration(UpdateConfigurationModel updateConfigurationModel) throws UnsupportedEncodingException, KeeperException, InterruptedException {
        String path = "/configurations/" + updateConfigurationModel.getDeploymentEnvironment() + "-" + updateConfigurationModel.getPartitionCode() + "-" + updateConfigurationModel.getServiceName() + "/" + updateConfigurationModel.getConfigurationKey();
        zooKeeper.setData(path, updateConfigurationModel.getConfigurationValue().getBytes(Constants.CHARSET_NAME_UTF_8), -1);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("修改配置成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 删除配置
     *
     * @param deleteConfigurationModel
     * @return
     * @throws KeeperException
     * @throws InterruptedException
     */
    public ApiRest deleteConfiguration(DeleteConfigurationModel deleteConfigurationModel) throws KeeperException, InterruptedException {
        String path = "/configurations/" + deleteConfigurationModel.getDeploymentEnvironment() + "-" + deleteConfigurationModel.getPartitionCode() + "-" + deleteConfigurationModel.getServiceName() + "/" + deleteConfigurationModel.getConfigurationKey();
        zooKeeper.delete(path, -1);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("删除配置成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
