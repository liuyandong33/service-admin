package build.dream.admin.models.host;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateModel extends BasicModel {
    @NotNull
    private Long id;

    @NotNull
    @Min(value = 10)
    private Integer diskSize;

    @NotNull
    @Min(value = 1)
    private Integer cpuCoreQuantity;

    @NotNull
    @Min(value = 10485760)
    private Integer memorySize;

    @NotNull
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public Integer getCpuCoreQuantity() {
        return cpuCoreQuantity;
    }

    public void setCpuCoreQuantity(Integer cpuCoreQuantity) {
        this.cpuCoreQuantity = cpuCoreQuantity;
    }

    public Integer getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Integer memorySize) {
        this.memorySize = memorySize;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
