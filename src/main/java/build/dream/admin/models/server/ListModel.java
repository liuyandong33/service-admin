package build.dream.admin.models.server;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class ListModel extends BasicModel {
    @NotNull
    private BigInteger hostId;

    @NotNull
    @Min(value = 1)
    private Integer page;

    @NotNull
    @Min(value = 1)
    @Max(value = 50)
    private Integer rows;

    public BigInteger getHostId() {
        return hostId;
    }

    public void setHostId(BigInteger hostId) {
        this.hostId = hostId;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
