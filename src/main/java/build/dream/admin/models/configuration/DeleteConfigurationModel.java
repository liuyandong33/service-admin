package build.dream.admin.models.configuration;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class DeleteConfigurationModel extends BasicModel {
    @NotNull
    private String deploymentEnvironment;

    @NotNull
    private String partitionCode;

    @NotNull
    private String serviceName;

    @NotNull
    private String configurationKey;

    public String getDeploymentEnvironment() {
        return deploymentEnvironment;
    }

    public void setDeploymentEnvironment(String deploymentEnvironment) {
        this.deploymentEnvironment = deploymentEnvironment;
    }

    public String getPartitionCode() {
        return partitionCode;
    }

    public void setPartitionCode(String partitionCode) {
        this.partitionCode = partitionCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getConfigurationKey() {
        return configurationKey;
    }

    public void setConfigurationKey(String configurationKey) {
        this.configurationKey = configurationKey;
    }
}
