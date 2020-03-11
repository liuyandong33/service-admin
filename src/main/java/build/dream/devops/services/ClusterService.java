package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.devops.Cluster;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import build.dream.devops.constants.Constants;
import build.dream.devops.models.cluster.ListClustersModel;
import build.dream.devops.models.cluster.SaveClusterModel;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClusterService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveCluster(SaveClusterModel saveClusterModel) {
        Long userId = saveClusterModel.obtainUserId();
        Long id = saveClusterModel.getId();
        String name = saveClusterModel.getName();
        Integer type = saveClusterModel.getType();

        Cluster cluster = null;
        if (id != null) {
            cluster = DatabaseHelper.find(Cluster.class, id);
            Validate.notNull(cluster, "集群不存在！");

            cluster.setName(name);
            cluster.setUpdatedUserId(userId);
            cluster.setType(type);
            cluster.setUpdatedRemark("修改集群信息！");
            DatabaseHelper.update(cluster);
        } else {
            cluster = Cluster.builder()
                    .name(name)
                    .type(type)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增集群信息")
                    .build();
            DatabaseHelper.insert(cluster);
        }

        return new ApiRest(cluster, "保存集群成功！");
    }

    @Transactional(readOnly = true)
    public ApiRest listClusters(ListClustersModel listClustersModel) {
        int page = listClustersModel.getPage();
        int rows = listClustersModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition(Cluster.ColumnName.DELETED, Constants.SQL_OPERATION_SYMBOL_EQUAL, 0));
        SearchModel searchModel = SearchModel.builder()
                .searchConditions(searchConditions)
                .build();
        long count = DatabaseHelper.count(Cluster.class, searchModel);

        List<Cluster> clusters = null;
        if (count > 0) {
            PagedSearchModel pagedSearchModel = PagedSearchModel.builder()
                    .searchConditions(searchConditions)
                    .page(page)
                    .rows(rows)
                    .build();
            clusters = DatabaseHelper.findAllPaged(Cluster.class, pagedSearchModel);
        } else {
            clusters = new ArrayList<Cluster>();
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", clusters);
        return new ApiRest(data, "查询集群列表成功！");
    }
}
