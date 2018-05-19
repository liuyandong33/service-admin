package build.dream.admin.services;

import build.dream.admin.models.host.AddModel;
import build.dream.admin.models.host.ListModel;
import build.dream.common.api.ApiRest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HostService {
    /**
     * 添加主机
     *
     * @param addModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest add(AddModel addModel) {
        return new ApiRest();
    }

    /**
     * 查询主机列表
     *
     * @param listModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest list(ListModel listModel) {
        return new ApiRest();
    }
}
