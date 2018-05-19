package build.dream.admin.models.host;

import build.dream.common.models.BasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class SaveModel extends BasicModel {
    private BigInteger id;

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
    private String userName;

    @NotNull
    private String password;

    @NotNull
    private BigInteger userId;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
