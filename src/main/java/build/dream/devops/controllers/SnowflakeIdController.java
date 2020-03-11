package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.devops.models.snowflakeid.SaveSnowflakeIdConfigurationModel;
import build.dream.devops.services.SnowflakeIdService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/snowflakeId")
public class SnowflakeIdController {
    /**
     * 保存雪花算法配置
     *
     * @return
     */
    @RequestMapping(value = "/saveSnowflakeIdConfiguration", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveSnowflakeIdConfigurationModel.class, serviceClass = SnowflakeIdService.class, serviceMethodName = "saveSnowflakeIdConfiguration", error = "保存雪花算法配置失败")
    public String saveSnowflakeIdConfiguration() {
        return null;
    }
}
