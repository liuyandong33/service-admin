package build.dream.admin.controllers;

import build.dream.admin.models.server.CreateServerModel;
import build.dream.admin.models.server.ListModel;
import build.dream.admin.services.ServerService;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/server")
public class ServerController {
    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/list")
    @ResponseBody
    public String list() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListModel listModel = ApplicationHandler.instantiateObject(ListModel.class, requestParameters);
            listModel.validateAndThrow();

            return serverService.list(listModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询服务器列表失败", requestParameters);
    }

    @RequestMapping(value = "/createServer")
    @ResponseBody
    public String createServer() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            CreateServerModel createServerModel = ApplicationHandler.instantiateObject(CreateServerModel.class, requestParameters);
            createServerModel.validateAndThrow();

            return serverService.createServer(createServerModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "创建服务器失败", requestParameters);
    }
}
