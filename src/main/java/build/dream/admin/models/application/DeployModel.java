package build.dream.admin.models.application;

import build.dream.common.constraints.InList;
import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class DeployModel extends BasicModel {
    @NotNull
    private Long appId;

    @NotNull
    @InList(value = {"snapshot", "release"})
    private String type;

    @NotNull
    private String version;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
