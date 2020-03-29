package build.dream.devops.models.log;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ClearLogsModel extends BasicModel {
    /**
     * 部署环境
     */
    @NotNull
    private String deploymentEnvironment;

    /**
     * 分区码
     */
    private String partitionCode;

    /**
     * 服务名称
     */
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
