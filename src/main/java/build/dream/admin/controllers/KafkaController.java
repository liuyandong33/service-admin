package build.dream.admin.controllers;

import build.dream.admin.models.kafka.*;
import build.dream.admin.services.KafkaService;
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
@RequestMapping(value = "/kafka")
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;

    /**
     * 获取所有 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/listNodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listNodes() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListNodesModel listNodesModel = ApplicationHandler.instantiateObject(ListNodesModel.class, requestParameters);
            listNodesModel.validateAndThrow();

            return kafkaService.listNodes(listNodesModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取 Kafka 节点失败", requestParameters);
    }

    /**
     * 启动 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String start() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            StartModel startModel = ApplicationHandler.instantiateObject(StartModel.class, requestParameters);
            startModel.validateAndThrow();

            return kafkaService.start(startModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "启动 Kafka 失败", requestParameters);
    }

    /**
     * 停止 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String stop() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            StopModel stopModel = ApplicationHandler.instantiateObject(StopModel.class, requestParameters);
            stopModel.validateAndThrow();

            return kafkaService.stop(stopModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "停止 Kafka 失败", requestParameters);
    }

    /**
     * 重启 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/restart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String restart() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            RestartModel restartModel = ApplicationHandler.instantiateObject(RestartModel.class, requestParameters);
            restartModel.validateAndThrow();

            return kafkaService.restart(restartModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "重启 Kafka 失败", requestParameters);
    }

    /**
     * 获取 zookeeper 节点状态
     *
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String status() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            StatusModel statusModel = ApplicationHandler.instantiateObject(StatusModel.class, requestParameters);
            statusModel.validateAndThrow();

            return kafkaService.status(statusModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "获取 Kafka 节点状态失败", requestParameters);
    }

    /**
     * 保存 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/saveNode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveNode() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveNodeModel saveNodeModel = ApplicationHandler.instantiateObject(SaveNodeModel.class, requestParameters);
            saveNodeModel.validateAndThrow();

            return kafkaService.saveNode(saveNodeModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存 Kafka 节点失败", requestParameters);
    }
}
