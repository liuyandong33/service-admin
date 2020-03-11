package build.dream.devops.models.service;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class DeleteConfigurationModel extends BasicModel {
    @NotNull
    private Long serviceId;

    @NotNull
    private String configurationKey;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getConfigurationKey() {
        return configurationKey;
    }

    public void setConfigurationKey(String configurationKey) {
        this.configurationKey = configurationKey;
    }
}
