package build.dream.admin.controllers;

import build.dream.admin.models.zookeeper.ListNodesModel;
import build.dream.admin.models.zookeeper.RestartModel;
import build.dream.admin.models.zookeeper.StartModel;
import build.dream.admin.models.zookeeper.StopModel;
import build.dream.admin.services.ZookeeperService;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/zookeeper")
public class ZookeeperController {
    @Autowired
    private ZookeeperService zookeeperService;

    @RequestMapping(value = "/listNodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listNodes() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListNodesModel listNodesModel = ApplicationHandler.instantiateObject(ListNodesModel.class, requestParameters);
            listNodesModel.validateAndThrow();

            return zookeeperService.listNodes(listNodesModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取 Zookeeper 节点失败", requestParameters);
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String start() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            StartModel startModel = ApplicationHandler.instantiateObject(StartModel.class, requestParameters);
            startModel.validateAndThrow();

            return zookeeperService.start(startModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "启动 Zookeeper 失败", requestParameters);
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String stop() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            StopModel stopModel = ApplicationHandler.instantiateObject(StopModel.class, requestParameters);
            stopModel.validateAndThrow();

            return zookeeperService.stop(stopModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "停止 Zookeeper 失败", requestParameters);
    }

    @RequestMapping(value = "/restart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String restart() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            RestartModel restartModel = ApplicationHandler.instantiateObject(RestartModel.class, requestParameters);
            restartModel.validateAndThrow();

            return zookeeperService.restart(restartModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "重启 Zookeeper 失败", requestParameters);
    }
}
