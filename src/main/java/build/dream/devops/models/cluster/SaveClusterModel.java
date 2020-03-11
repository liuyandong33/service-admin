package build.dream.devops.models.cluster;

import build.dream.common.models.DevOpsBasicModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.devops.constants.Constants;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SaveClusterModel extends DevOpsBasicModel {
    private static final Integer[] TYPES = {Constants.CLUSTER_TYPE_ZOOKEEPER};

    private Long id;

    @NotNull
    @Length(max = 20)
    private String name;

    @NotNull
    private Integer type;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean validate() {
        return super.validate() && ArrayUtils.contains(TYPES, type);
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        ApplicationHandler.inArray(TYPES, type, "type");
    }
}
