package build.dream.admin.models.host;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class UndefineModel extends BasicModel {
    @NotNull
    private Long hostId;

    @NotNull
    private Long userId;

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
