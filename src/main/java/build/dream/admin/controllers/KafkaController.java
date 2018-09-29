package build.dream.admin.controllers;

import build.dream.admin.models.kafka.ListNodesModel;
import build.dream.admin.models.kafka.SaveNodeModel;
import build.dream.admin.models.kafka.StartModel;
import build.dream.admin.models.kafka.StopModel;
import build.dream.admin.services.KafkaService;
import build.dream.common.annotations.ApiRestAction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/kafka")
public class KafkaController {
    /**
     * 获取所有 kafka 节点
     *
     * @return
     */
    @RequestMapping(value = "/listNodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListNodesModel.class, serviceClass = KafkaService.class, serviceMethodName = "listNodes", error = "获取Kafka节点失败")
    public String listNodes() {
        return null;
    }

    /**
     * 启动 kafka 节点
     *
     * @return
     */
    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = StartModel.class, serviceClass = KafkaService.class, serviceMethodName = "start", error = "启动Kafka失败")
    public String start() {
        return null;
    }

    /**
     * 停止 kafka 节点
     *
     * @return
     */
    @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = StopModel.class, serviceClass = KafkaService.class, serviceMethodName = "stop", error = "停止Kafka失败")
    public String stop() {
        return null;
    }


    /**
     * 保存 kafka 节点
     *
     * @return
     */
    @RequestMapping(value = "/saveNode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveNodeModel.class, serviceClass = KafkaService.class, serviceMethodName = "saveNode", error = "保存Kafka节点失败")
    public String saveNode() {
        return null;
    }
}
