package build.dream.devops.models.cluster;

import build.dream.common.models.DevOpsBasicModel;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ListClustersModel extends DevOpsBasicModel {
    @NotNull
    @Min(value = 1)
    private Integer page;

    @NotNull
    @Min(value = 1)
    @Max(value = 50)
    private Integer rows;

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