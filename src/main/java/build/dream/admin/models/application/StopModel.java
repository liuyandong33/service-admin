package build.dream.admin.models.application;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class StopModel extends BasicModel {
    @NotNull
    private Long appId;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
