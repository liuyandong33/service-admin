package build.dream.admin.controllers;

import build.dream.admin.services.MySqlService;
import build.dream.common.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/mySql")
public class MySqlController {
    @Autowired
    private MySqlService mySqlService;

    @RequestMapping(value = "/listConfigurations", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String listConfigurations() {
        return GsonUtils.toJson(mySqlService.listConfigurations());
    }
}
