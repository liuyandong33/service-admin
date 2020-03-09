package build.dream.devops.models.cluster;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class SaveModel extends BasicModel {
    private static final Integer[] TYPES = {1};

    private Long id;

    @NotNull
    @Length(max = 20)
    private String name;

    private Integer type;

    private Long tenantId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public void validateAndThrow() {
        super.validateAndThrow();
        if (id == null) {
            ApplicationHandler.notNull(type, "type");
            ApplicationHandler.inArray(TYPES, type, "type");

            ApplicationHandler.notNull(tenantId, "tenantId");
        }
    }
}
