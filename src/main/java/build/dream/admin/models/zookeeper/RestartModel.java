package build.dream.admin.models.zookeeper;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class RestartModel extends BasicModel {
    @NotNull
    private Long nodeId;

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
}
