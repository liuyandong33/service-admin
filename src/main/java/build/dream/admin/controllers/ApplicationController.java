package build.dream.admin.controllers;

import build.dream.admin.models.application.DeployModel;
import build.dream.admin.models.application.StopModel;
import build.dream.admin.services.ApplicationService;
import build.dream.common.annotations.ApiRestAction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application")
public class ApplicationController {
    @RequestMapping(value = "/deploy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DeployModel.class, serviceClass = ApplicationService.class, serviceMethodName = "deploy", error = "部署应用失败")
    public String deploy() {
        return null;
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = StopModel.class, serviceClass = ApplicationService.class, serviceMethodName = "stop", error = "停止应用失败")
    public String stop() {
        return null;
    }
}
