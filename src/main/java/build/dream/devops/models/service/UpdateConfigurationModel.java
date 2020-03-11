package build.dream.devops.models.service;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class UpdateConfigurationModel extends BasicModel {
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
