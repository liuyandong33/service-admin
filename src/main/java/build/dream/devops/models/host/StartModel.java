package build.dream.devops.models.host;

import build.dream.common.models.DevOpsBasicModel;

import javax.validation.constraints.NotNull;

public class StartModel extends DevOpsBasicModel {
    @NotNull
    private Long hostId;

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }
}
