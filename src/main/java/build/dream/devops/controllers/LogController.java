package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.annotations.PermitAll;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.JacksonUtils;
import build.dream.devops.models.log.ClearLogsModel;
import build.dream.devops.models.log.ListLogsModel;
import build.dream.devops.services.LogService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

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

    @RequestMapping(value = "/index")
    public String index() {
        return "log/index";
    }

    @RequestMapping(value = "/obtainLog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String obtainLog() throws IOException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        int start = Integer.parseInt(requestParameters.get("start"));
        InputStream inputStream = new FileInputStream("/Users/liuyandong/Desktop/devops.log");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        int available = inputStream.available();

        Map<String, Object> data = null;
        if (available > start) {
            data = new HashMap<String, Object>();
            inputStreamReader.skip(start);

            int length = available - start;
            if (length > 1024) {
                length = 1024;
            }

            char[] buffer = new char[length];
            inputStreamReader.read(buffer, 0, length);
            data.put("length", length);
            data.put("log", buffer);
        }
        return JacksonUtils.writeValueAsString(ApiRest.builder().data(data).message("获取日志成功！").successful(true).build());
    }
}
