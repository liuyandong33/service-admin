package build.dream.admin.models.host;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CreateHostModel extends BasicModel {
    @NotNull
    Long hostId;

    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    private String password;

    @NotNull
    @Min(value = 10)
    private Integer diskSize;

    @NotNull
    @Min(value = 1)
    private Integer cpuCoreQuantity;

    @NotNull
    @Min(value = 1024)
    private Integer memorySize;

    @NotNull
    private Long userId;

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
