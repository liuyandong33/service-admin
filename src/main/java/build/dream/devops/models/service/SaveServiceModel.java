package build.dream.devops.models.service;

import build.dream.common.models.DevOpsBasicModel;
import build.dream.common.utils.ApplicationHandler;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class SaveServiceModel extends DevOpsBasicModel {
    private Long id;
    @NotNull
    private Long appId;
    /**
     * 名字
     */
    @NotNull
    private String name;

    /**
     * 程序名称
     */
    @NotNull
    private String programName;

    /**
     * 程序版本
     */
    @NotNull
    private String programVersion;

    /**
     * 服务端口
     */
    private Integer port;

    @NotNull
    private String healthCheckPath;

    /**
     * 是否分区
     */
    @NotNull
    private Boolean partitioned;

    /**
     * 部署环境
     */
    @NotNull
    @Length(max = 255)
    private String deploymentEnvironment;

    /**
     * 分区码
     */
    @Length(max = 20)
    private String partitionCode;

    /**
     * 服务名称
     */
    @NotNull
    @Length(max = 255)
    private String serviceName;

    /**
     * zookeeper 连接字符串
     */
    @NotNull
    @Length(max = 255)
    private String zookeeperConnectString;

    private Map<String, Object> javaOpts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getProgramVersion() {
        return programVersion;
    }

    public void setProgramVersion(String programVersion) {
        this.programVersion = programVersion;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHealthCheckPath() {
        return healthCheckPath;
    }

    public void setHealthCheckPath(String healthCheckPath) {
        this.healthCheckPath = healthCheckPath;
    }

    public Boolean getPartitioned() {
        return partitioned;
    }

    public void setPartitioned(Boolean partitioned) {
        this.partitioned = partitioned;
    }

    public String getDeploymentEnvironment() {
        return deploymentEnvironment;
    }

    public void setDeploymentEnvironment(String deploymentEnvironment) {
        this.deploymentEnvironment = deploymentEnvironment;
    }

    public String getPartitionCode() {
        return partitionCode;
    }

    public void setPartitionCode(String partitionCode) {
        this.partitionCode = partitionCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getZookeeperConnectString() {
        return zookeeperConnectString;
    }

    public void setZookeeperConnectString(String zookeeperConnectString) {
        this.zookeeperConnectString = zookeeperConnectString;
    }

    public Map<String, Object> getJavaOpts() {
        return javaOpts;
    }

    public void setJavaOpts(Map<String, Object> javaOpts) {
        this.javaOpts = javaOpts;
    }

    @Override
    public boolean validate() {
        boolean isOk = super.validate();
        if (!partitioned) {
            return isOk;
        }
        return isOk && StringUtils.isNotBlank(partitionCode);
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        if (partitioned) {
            ApplicationHandler.notBlank(partitionCode, "partitionCode");
        }
    }
}
