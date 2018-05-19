package build.dream.admin.models.server;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class CreateServerModel extends BasicModel {
    @NotNull
    private BigInteger hostId;

    public BigInteger getHostId() {
        return hostId;
    }

    public void setHostId(BigInteger hostId) {
        this.hostId = hostId;
    }
}
