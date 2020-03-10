package build.dream.devops.models.service;

import build.dream.common.models.DevOpsBasicModel;

import javax.validation.constraints.NotNull;

public class DeployModel extends DevOpsBasicModel {
    @NotNull
    private Long serviceId;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
