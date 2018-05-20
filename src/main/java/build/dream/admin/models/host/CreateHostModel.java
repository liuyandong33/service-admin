package build.dream.admin.models.host;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class CreateHostModel extends BasicModel {
    @NotNull
    BigInteger hostId;

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
    private Integer cupCoreQuantity;

    @NotNull
    @Min(value = 10485760)
    private Integer memorySize;

    @NotNull
    private BigInteger userId;

    public BigInteger getHostId() {
        return hostId;
    }

    public void setHostId(BigInteger hostId) {
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
