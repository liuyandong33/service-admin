package build.dream.devops.models.snowflakeid;

import build.dream.common.models.DevOpsBasicModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SaveSnowflakeIdConfigurationModel extends DevOpsBasicModel {
    /**
     * work id
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 32)
    private Integer workerId;

    /**
     * data center id
     */
    @NotNull
    @Min(value = 0)
    @Max(value = 32)
    private Integer dataCenterId;

    /**
     * ip 地址
     */
    @NotNull
    @Length(max = 50)
    private String ipAddress;

    /**
     * 应用名称
     */
    @NotNull
    @Length(max = 50)
    private String applicationName;

    @NotNull
    @Length(max = 255)
    private String description;

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Integer getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(Integer dataCenterId) {
        this.dataCenterId = dataCenterId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
