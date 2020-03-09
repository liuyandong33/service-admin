package build.dream.devops.models.configuration;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class AddConfigurationModel extends BasicModel {
    @NotNull
    private String deploymentEnvironment;

    @NotNull
    private String partitionCode;

    @NotNull
    private String serviceName;

    @NotNull
    private String configurationKey;

    @NotNull
    private String configurationValue;

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

    public String getConfigurationValue() {
        return configurationValue;
    }

    public void setConfigurationValue(String configurationValue) {
        this.configurationValue = configurationValue;
    }
}
