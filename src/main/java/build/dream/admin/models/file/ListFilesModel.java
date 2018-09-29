package build.dream.admin.models.file;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class ListFilesModel extends BasicModel {
    @NotNull
    public String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
