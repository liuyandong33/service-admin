package build.dream.admin.services;

import build.dream.admin.models.server.CreateServerModel;
import build.dream.admin.models.server.ListModel;
import build.dream.common.api.ApiRest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerService {
    @Transactional(readOnly = true)
    public ApiRest list(ListModel listModel) {
        return new ApiRest();
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest createServer(CreateServerModel createServerModel) {
        return new ApiRest();
    }
}
