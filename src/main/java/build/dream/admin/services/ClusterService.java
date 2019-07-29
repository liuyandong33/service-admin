package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.cluster.ListModel;
import build.dream.admin.models.cluster.SaveModel;
import build.dream.common.admin.domains.Cluster;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClusterService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest save(SaveModel saveModel) {
        BigInteger id = saveModel.getId();
        String name = saveModel.getName();
        Integer type = saveModel.getType();
        BigInteger tenantId = saveModel.getTenantId();
        BigInteger userId = saveModel.getUserId();

        Cluster cluster = null;
        if (id != null) {
            cluster = DatabaseHelper.find(Cluster.class, id);
            Validate.notNull(cluster, "集群不存在！");

            cluster.setName(name);
            cluster.setUpdatedUserId(userId);
            cluster.setUpdatedRemark("修改集群信息！");
            DatabaseHelper.update(cluster);
        } else {
            cluster = new Cluster();
            cluster.setName(name);
            cluster.setType(type);
            cluster.setType(type);
            cluster.setTenantId(tenantId);
            cluster.setCreatedUserId(userId);
            cluster.setUpdatedUserId(userId);
            cluster.setUpdatedRemark("新增集群信息！");
            DatabaseHelper.insert(cluster);
        }

        return new ApiRest(cluster, "保存集群成功！");
    }

    @Transactional(readOnly = true)
    public ApiRest list(ListModel listModel) {
        BigInteger tenantId = listModel.getTenantId();
        int page = listModel.getPage();
        int rows = listModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition("tenant_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, tenantId));
        SearchModel searchModel = new SearchModel(true);
        searchModel.setSearchConditions(searchConditions);
        long count = DatabaseHelper.count(Cluster.class, searchModel);

        List<Cluster> clusters = new ArrayList<Cluster>();
        if (count > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel(true);
            pagedSearchModel.setPage(page);
            pagedSearchModel.setRows(rows);
            pagedSearchModel.setSearchConditions(searchConditions);
            clusters = DatabaseHelper.findAllPaged(Cluster.class, pagedSearchModel);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", clusters);
        return new ApiRest(data, "查询集群列表成功！");
    }
}
