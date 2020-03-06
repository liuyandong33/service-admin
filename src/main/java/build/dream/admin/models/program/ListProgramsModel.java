package build.dream.admin.models.program;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListProgramsModel extends BasicModel {
    @NotNull
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
