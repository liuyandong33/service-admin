package build.dream.admin.models.host;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class UpdateModel extends BasicModel {
    @NotNull
    private BigInteger id;

    @NotNull
    private Integer diskSize;

    @NotNull
    private Integer cupCoreQuantity;

    @NotNull
    private Integer memorySize;

    @NotNull
    private BigInteger userId;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public Integer getCupCoreQuantity() {
        return cupCoreQuantity;
    }

    public void setCupCoreQuantity(Integer cupCoreQuantity) {
        this.cupCoreQuantity = cupCoreQuantity;
    }

    public Integer getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Integer memorySize) {
        this.memorySize = memorySize;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
