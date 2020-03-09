package build.dream.devops.models.service;

import build.dream.common.models.weixinpay.DevOpsBasicModel;

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

    private Map<String, String> configurations;

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

    public Map<String, String> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Map<String, String> configurations) {
        this.configurations = configurations;
    }

    public Map<String, Object> getJavaOpts() {
        return javaOpts;
    }

    public void setJavaOpts(Map<String, Object> javaOpts) {
        this.javaOpts = javaOpts;
    }
}
