package build.dream.devops.controllers;

import build.dream.devops.models.configuration.AddConfigurationModel;
import build.dream.devops.models.configuration.DeleteConfigurationModel;
import build.dream.devops.models.configuration.ListConfigurationsModel;
import build.dream.devops.models.configuration.UpdateConfigurationModel;
import build.dream.devops.services.ConfigurationService;
import build.dream.common.annotations.ApiRestAction;
import build.dream.common.controllers.BasicController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/configuration")
public class ConfigurationController extends BasicController {
    /**
     * 新增配置
     *
     * @return
     */
    @RequestMapping(value = "/addConfiguration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = AddConfigurationModel.class, serviceClass = ConfigurationService.class, serviceMethodName = "addConfiguration", error = "新增配置失败")
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
    @ApiRestAction(modelClass = ListConfigurationsModel.class, serviceClass = ConfigurationService.class, serviceMethodName = "listConfigurations", error = "查询配置失败")
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
    @ApiRestAction(modelClass = UpdateConfigurationModel.class, serviceClass = ConfigurationService.class, serviceMethodName = "updateConfiguration", error = "修改配置失败")
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
    @ApiRestAction(modelClass = DeleteConfigurationModel.class, serviceClass = ConfigurationService.class, serviceMethodName = "deleteConfiguration", error = "删除配置失败")
    public String deleteConfiguration() {
        return null;
    }
}
