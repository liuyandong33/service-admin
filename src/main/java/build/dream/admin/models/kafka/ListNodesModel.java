package build.dream.admin.models.kafka;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ListNodesModel extends BasicModel {
    @NotNull
    private BigInteger clusterId;

    public BigInteger getClusterId() {
        return clusterId;
    }

    public void setClusterId(BigInteger clusterId) {
        this.clusterId = clusterId;
    }
}
