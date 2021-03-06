package build.dream.admin.models.zookeeper;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class RestartModel extends BasicModel {
    @NotNull
    private BigInteger nodeId;

    public BigInteger getNodeId() {
        return nodeId;
    }

    public void setNodeId(BigInteger nodeId) {
        this.nodeId = nodeId;
    }
}
