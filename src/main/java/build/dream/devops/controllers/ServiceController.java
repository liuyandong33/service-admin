package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.devops.models.service.*;
import build.dream.devops.services.ServiceService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/service")
public class ServiceController {
    /**
     * 分页查询服务
     *
     * @return
     */
    @RequestMapping(value = "/listServices", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListServicesModel.class, serviceClass = ServiceService.class, serviceMethodName = "listServices", error = "查询服务列表失败")
    public String listServices() {
        return null;
    }

    /**
     * 获取服务详细信息
     *
     * @return
     */
    @RequestMapping(value = "/obtainServiceInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ObtainServiceInfoModel.class, serviceClass = ServiceService.class, serviceMethodName = "obtainServiceInfo", error = "获取服务信息失败")
    public String obtainServiceInfo() {
        return null;
    }

    /**
     * 保存服务
     *
     * @return
     */
    @RequestMapping(value = "/saveService", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveServiceModel.class, serviceClass = ServiceService.class, serviceMethodName = "saveService", error = "保存服务失败")
    public String saveService() {
        return null;
    }

    /**
     * 服务部署
     *
     * @return
     */
    @RequestMapping(value = "/deploy", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DeployModel.class, serviceClass = ServiceService.class, serviceMethodName = "deploy", error = "部署失败")
    public String deploy() {
        return null;
    }

    /**
     * 新增配置
     *
     * @return
     */
    @RequestMapping(value = "/addConfiguration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = AddConfigurationModel.class, serviceClass = ServiceService.class, serviceMethodName = "addConfiguration", error = "新增配置失败")
    public String addConfiguration() {
        return null;
    }

    /**
     * 查询所有配置
     *
     * @return
     */
    @RequestMapping(value = "/listConfigurations", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListConfigurationsModel.class, serviceClass = ServiceService.class, serviceMethodName = "listConfigurations", error = "查询配置失败")
    public String listConfigurations() {
        return null;
    }

    /**
     * 修改配置
     *
     * @return
     */
    @RequestMapping(value = "/updateConfiguration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = UpdateConfigurationModel.class, serviceClass = ServiceService.class, serviceMethodName = "updateConfiguration", error = "修改配置失败")
    public String updateConfiguration() {
        return null;
    }

    /**
     * 删除配置
     *
     * @return
     */
    @RequestMapping(value = "/deleteConfiguration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = DeleteConfigurationModel.class, serviceClass = ServiceService.class, serviceMethodName = "deleteConfiguration", error = "删除配置失败")
    public String deleteConfiguration() {
        return null;
    }
}
