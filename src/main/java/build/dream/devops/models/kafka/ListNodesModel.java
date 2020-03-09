package build.dream.devops.models.kafka;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListNodesModel extends BasicModel {
    @NotNull
    private Long clusterId;

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }
}
