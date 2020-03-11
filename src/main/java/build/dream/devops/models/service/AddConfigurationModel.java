package build.dream.devops.models.service;

import build.dream.common.models.DevOpsBasicModel;

import javax.validation.constraints.NotNull;

public class AddConfigurationModel extends DevOpsBasicModel {
    @NotNull
    private Long serviceId;

    @NotNull
    private String configurationKey;

    @NotNull
    private String configurationValue;

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

    public String getConfigurationValue() {
        return configurationValue;
    }

    public void setConfigurationValue(String configurationValue) {
        this.configurationValue = configurationValue;
    }
}
