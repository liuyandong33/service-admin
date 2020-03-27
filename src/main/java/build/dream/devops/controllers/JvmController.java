package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.annotations.PermitAll;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.JacksonUtils;
import build.dream.common.utils.JvmUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/jvm")
@PermitAll
public class JvmController {
    @RequestMapping(value = "/{methodName}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "处理失败")
    public String call(@PathVariable String methodName) {
        return JacksonUtils.writeValueAsString(ApiRest.builder().data(JvmUtils.call(methodName)).message("处理成功！").successful(true).build());
    }
}
