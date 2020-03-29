package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.annotations.PermitAll;
import build.dream.devops.models.log.ClearLogsModel;
import build.dream.devops.models.log.ListLogsModel;
import build.dream.devops.services.LogService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/log")
@PermitAll
public class LogController {
    /**
     * 分页查询日志
     *
     * @return
     */
    @RequestMapping(value = "/listLogs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListLogsModel.class, serviceClass = LogService.class, serviceMethodName = "listLogs", error = "查询日志失败")
    public String listLogs() {
        return null;
    }

    /**
     * 清除日志
     *
     * @return
     */
    @RequestMapping(value = "/clearLogs", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ClearLogsModel.class, serviceClass = LogService.class, serviceMethodName = "clearLogs", error = "清除日志失败")
    public String clearLogs() {
        return null;
    }
}
