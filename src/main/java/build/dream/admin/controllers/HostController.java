package build.dream.admin.controllers;

import build.dream.admin.models.host.*;
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

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    @RequestMapping(value = "/createHost", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

    /**
     * 开机
     *
     * @return
     */
    @RequestMapping(value = "/start", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String start() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            StartModel startModel = ApplicationHandler.instantiateObject(StartModel.class, requestParameters);
            startModel.validateAndThrow();

            return hostService.start(startModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "开机失败", requestParameters);
    }

    /**
     * 关机
     *
     * @return
     */
    @RequestMapping(value = "/shutdown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String shutdown() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ShutdownModel shutdownModel = ApplicationHandler.instantiateObject(ShutdownModel.class, requestParameters);
            shutdownModel.validateAndThrow();

            return hostService.shutdown(shutdownModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "关机失败", requestParameters);
    }

    /**
     * 关机
     *
     * @return
     */
    @RequestMapping(value = "/destroy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String destroy() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            DestroyModel destroyModel = ApplicationHandler.instantiateObject(DestroyModel.class, requestParameters);
            destroyModel.validateAndThrow();

            return hostService.destroy(destroyModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "关机失败", requestParameters);
    }

    /**
     * 删除虚拟机
     *
     * @return
     */
    @RequestMapping(value = "/undefine", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String undefine() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            UndefineModel undefineModel = ApplicationHandler.instantiateObject(UndefineModel.class, requestParameters);
            undefineModel.validateAndThrow();

            return hostService.undefine(undefineModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "删除虚拟机失败", requestParameters);
    }

    /**
     * 更新失败
     *
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String update() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            UpdateModel updateModel = ApplicationHandler.instantiateObject(UpdateModel.class, requestParameters);
            updateModel.validateAndThrow();

            return hostService.update(updateModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "更新失败", requestParameters);
    }
}
