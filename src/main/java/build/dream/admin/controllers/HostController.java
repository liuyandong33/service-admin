package build.dream.admin.controllers;

import build.dream.admin.models.host.CreateHostModel;
import build.dream.admin.models.host.ListModel;
import build.dream.admin.models.host.SaveModel;
import build.dream.admin.services.HostService;
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
@RequestMapping(value = "/host")
public class HostController {
    @Autowired
    private HostService hostService;

    @RequestMapping(value = "/save", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String save() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveModel saveModel = ApplicationHandler.instantiateObject(SaveModel.class, requestParameters);
            saveModel.validateAndThrow();

            return hostService.save(saveModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存失败", requestParameters);
    }

    @RequestMapping(value = "/createHost", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String createHost() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            CreateHostModel createHostModel = ApplicationHandler.instantiateObject(CreateHostModel.class, requestParameters);
            createHostModel.validateAndThrow();

            return hostService.createHost(createHostModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "创建主机失败", requestParameters);
    }

    /**
     * 查询主机列表
     *
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public String list() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListModel listModel = ApplicationHandler.instantiateObject(ListModel.class, requestParameters);
            listModel.validateAndThrow();

            return hostService.list(listModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询主机列表失败", requestParameters);
    }
}
