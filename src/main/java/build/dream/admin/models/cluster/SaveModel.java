package build.dream.admin.models.cluster;

import build.dream.common.models.BasicModel;
import build.dream.common.utils.ApplicationHandler;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class SaveModel extends BasicModel {
    private static final Integer[] TYPES = {1};

    private BigInteger id;

    @NotNull
    @Length(max = 20)
    private String name;

    private Integer type;

    private BigInteger tenantId;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
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
