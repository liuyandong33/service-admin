package build.dream.admin.controllers;

import build.dream.admin.models.zookeeper.*;
import build.dream.admin.services.ZookeeperService;
import build.dream.common.annotations.ApiRestAction;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/zookeeper")
public class ZookeeperController {
    /**
     * 获取所有 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/listNodes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListNodesModel.class, serviceClass = ZookeeperService.class, serviceMethodName = "listNodes", error = "获取Zookeeper节点失败")
    public String listNodes() {
        return null;
    }

    /**
     * 启动 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = StartModel.class, serviceClass = ZookeeperService.class, serviceMethodName = "start", error = "启动Zookeeper失败")
    public String start() {
        return null;
    }

    /**
     * 停止 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/stop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = StopModel.class, serviceClass = ZookeeperService.class, serviceMethodName = "stop", error = "停止Zookeeper失败")
    public String stop() {
        return null;
    }

    /**
     * 重启 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/restart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = RestartModel.class, serviceClass = ZookeeperService.class, serviceMethodName = "restart", error = "重启Zookeeper失败")
    public String restart() {
        return null;
    }

    /**
     * 获取 zookeeper 节点状态
     *
     * @return
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = StatusModel.class, serviceClass = ZookeeperService.class, serviceMethodName = "status", error = "获取Zookeeper节点状态失败")
    public String status() {
        return null;
    }

    /**
     * 保存 zookeeper 节点
     *
     * @return
     */
    @RequestMapping(value = "/saveNode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveNodeModel.class, serviceClass = ZookeeperService.class, serviceMethodName = "saveNode", error = "保存Zookeeper节点失败")
    public String saveNode() {
        return null;
    }
}
