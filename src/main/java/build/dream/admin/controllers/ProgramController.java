package build.dream.admin.controllers;

import build.dream.admin.models.program.ListProgramsModel;
import build.dream.admin.models.program.ListVersionsModel;
import build.dream.admin.services.ProgramService;
import build.dream.common.annotations.ApiRestAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/program")
public class ProgramController {
    @RequestMapping(value = "/listPrograms")
    @ResponseBody
    @ApiRestAction(modelClass = ListProgramsModel.class, serviceClass = ProgramService.class, serviceMethodName = "listPrograms", error = "查询程序列表失败")
    public String listPrograms() {
        return null;
    }

    @RequestMapping(value = "/listVersions")
    @ResponseBody
    @ApiRestAction(modelClass = ListVersionsModel.class, serviceClass = ProgramService.class, serviceMethodName = "listVersions", error = "查询程序版本列表失败")
    public String listVersions() {
        return null;
    }
}
