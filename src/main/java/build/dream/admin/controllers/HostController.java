package build.dream.admin.controllers;

import build.dream.admin.models.host.AddModel;
import build.dream.admin.models.host.ListModel;
import build.dream.admin.services.HostService;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.MethodCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/host")
public class HostController {
    @Autowired
    private HostService hostService;

    /**
     * 添加主机
     *
     * @return
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public String add() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            AddModel addModel = ApplicationHandler.instantiateObject(AddModel.class, requestParameters);
            addModel.validateAndThrow();

            return hostService.add(addModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "添加主机列表失败", requestParameters);
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
