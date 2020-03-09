package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.devops.models.host.ListHostsModel;
import build.dream.devops.models.host.SaveHostModel;
import build.dream.devops.models.host.ShutdownModel;
import build.dream.devops.services.HostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/host")
public class HostController {
    @Autowired
    private HostService hostService;

    @RequestMapping(value = "/saveHost", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveHostModel.class, serviceClass = HostService.class, serviceMethodName = "saveHost", error = "保存主机失败")
    public String saveHost() {
        return null;
    }

    /**
     * 查询主机列表
     *
     * @return
     */
    @RequestMapping(value = "/listHosts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiRestAction(modelClass = ListHostsModel.class, serviceClass = HostService.class, serviceMethodName = "listHosts", error = "分页查询主机失败")
    @ResponseBody
    public String listHosts() {
        return null;
    }

    /**
     * 关机
     *
     * @return
     */
    @RequestMapping(value = "/shutdown", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiRestAction(modelClass = ShutdownModel.class, serviceClass = HostService.class, serviceMethodName = "shutdown", error = "关闭虚拟机失败")
    @ResponseBody
    public String shutdown() {
        return null;
    }
}
