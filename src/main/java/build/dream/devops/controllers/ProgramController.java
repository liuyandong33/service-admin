package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.devops.models.program.ListProgramsModel;
import build.dream.devops.models.program.ListVersionsModel;
import build.dream.devops.services.ProgramService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/program")
public class ProgramController {
    /**
     * 查询程序列表
     *
     * @return
     */
    @RequestMapping(value = "/listPrograms")
    @ResponseBody
    @ApiRestAction(modelClass = ListProgramsModel.class, serviceClass = ProgramService.class, serviceMethodName = "listPrograms", error = "查询程序列表失败")
    public String listPrograms() {
        return null;
    }

    /**
     * 查询程序版本
     *
     * @return
     */
    @RequestMapping(value = "/listVersions")
    @ResponseBody
    @ApiRestAction(modelClass = ListVersionsModel.class, serviceClass = ProgramService.class, serviceMethodName = "listVersions", error = "查询程序版本列表失败")
    public String listVersions() {
        return null;
    }
}
