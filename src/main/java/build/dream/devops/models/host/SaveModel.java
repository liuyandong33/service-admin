package build.dream.devops.models.host;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SaveModel extends BasicModel {
    private Long id;

    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    private String ipAddress;

    @NotNull
    @Min(value = 0)
    @Max(value = 65535)
    private Integer sshPort;

    @NotNull
    @Length(max = 20)
    private String userName;

    @NotNull
    @Length(max = 20)
    private String password;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getSshPort() {
        return sshPort;
    }

    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}
