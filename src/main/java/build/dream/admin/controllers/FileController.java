package build.dream.admin.controllers;

import build.dream.admin.models.file.ListFilesModel;
import build.dream.admin.services.FileService;
import build.dream.common.annotations.ApiRestAction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/file")
public class FileController {
    @RequestMapping(value = "/listFiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListFilesModel.class, serviceClass = FileService.class, serviceMethodName = "listFiles", error = "操作失败")
    public String listFiles() {
        return null;
    }
}
