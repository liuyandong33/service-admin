package build.dream.devops.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.devops.models.cluster.ListClustersModel;
import build.dream.devops.models.cluster.SaveClusterModel;
import build.dream.devops.services.ClusterService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/cluster")
public class ClusterController {
    @RequestMapping(value = "/saveCluster", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = SaveClusterModel.class, serviceClass = ClusterService.class, serviceMethodName = "saveCluster", error = "保存集群信息失败")
    public String saveCluster() {
        return null;
    }

    @RequestMapping(value = "/listClusters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListClustersModel.class, serviceClass = ClusterService.class, serviceMethodName = "listClusters", error = "查询集群信息失败")
    public String listClusters() {
        return null;
    }
}
