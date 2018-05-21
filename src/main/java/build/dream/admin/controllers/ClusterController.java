package build.dream.admin.controllers;

import build.dream.admin.models.cluster.ListModel;
import build.dream.admin.models.cluster.SaveModel;
import build.dream.admin.services.ClusterService;
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
@RequestMapping(value = "/cluster")
public class ClusterController {
    @Autowired
    private ClusterService clusterService;

    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String save() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            SaveModel saveModel = ApplicationHandler.instantiateObject(SaveModel.class, requestParameters);
            saveModel.validateAndThrow();

            return clusterService.save(saveModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "保存失败", requestParameters);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String list() {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        MethodCaller methodCaller = () -> {
            ListModel listModel = ApplicationHandler.instantiateObject(ListModel.class, requestParameters);
            listModel.validateAndThrow();

            return clusterService.list(listModel);
        };
        return ApplicationHandler.callMethod(methodCaller, "查询集群列表失败", requestParameters);
    }
}
