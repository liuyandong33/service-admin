package build.dream.admin.services;

import build.dream.admin.models.cluster.SaveModel;
import build.dream.admin.utils.DatabaseHelper;
import build.dream.common.admin.domains.Cluster;
import build.dream.common.api.ApiRest;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

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
            cluster.setLastUpdateUserId(userId);
            cluster.setLastUpdateRemark("修改集群信息！");
            DatabaseHelper.update(cluster);
        } else {
            cluster = new Cluster();
            cluster.setName(name);
            cluster.setType(type);
            cluster.setType(type);
            cluster.setTenantId(tenantId);
            cluster.setCreateUserId(userId);
            cluster.setLastUpdateUserId(userId);
            cluster.setLastUpdateRemark("新增集群信息！");
            DatabaseHelper.insert(cluster);
        }

        return new ApiRest(cluster, "保存集群成功！");
    }
}
