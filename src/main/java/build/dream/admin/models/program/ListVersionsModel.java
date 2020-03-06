package build.dream.admin.models.program;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListVersionsModel extends BasicModel {
    @NotNull
    private String type;

    @NotNull
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
