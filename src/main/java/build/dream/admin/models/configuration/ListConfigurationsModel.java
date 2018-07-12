package build.dream.admin.models.configuration;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListConfigurationsModel extends BasicModel {
    @NotNull
    private String deploymentEnvironment;

    @NotNull
    private String partitionCode;

    @NotNull
    private String serviceName;

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
}
