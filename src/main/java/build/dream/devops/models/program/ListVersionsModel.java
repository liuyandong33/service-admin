package build.dream.devops.models.program;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListVersionsModel extends BasicModel {
    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
