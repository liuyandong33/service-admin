package build.dream.devops.controllers;

import build.dream.devops.models.app.ListAppsModel;
import build.dream.devops.models.app.SaveAppModel;
import build.dream.devops.services.AppService;
import build.dream.common.annotations.ApiRestAction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/app")
public class AppController {
    @RequestMapping(value = "/listApps", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListAppsModel.class, serviceClass = AppService.class, serviceMethodName = "listApps", error = "查询应用列表失败")
    public String listApps() {
        return null;
    }

    @RequestMapping(value = "/saveApp", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveAppModel.class, serviceClass = AppService.class, serviceMethodName = "saveApp", error = "保存应用失败")
    public String saveApp() {
        return null;
    }
}
