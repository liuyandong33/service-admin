package build.dream.devops.models.zookeeper;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SaveNodeModel extends BasicModel {
    private Long id;

    @NotNull
    @Length(max = 20)
    private String hostName;

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
    @Length(max = 255)
    private String zookeeperHome;

    @NotNull
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public String getZookeeperHome() {
        return zookeeperHome;
    }

    public void setZookeeperHome(String zookeeperHome) {
        this.zookeeperHome = zookeeperHome;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
