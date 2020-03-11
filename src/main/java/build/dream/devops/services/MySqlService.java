package build.dream.devops.services;

import build.dream.common.domains.devops.MySqlConfiguration;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MySqlService {
    @Transactional(readOnly = true)
    public ApiRest listConfigurations() {
        SearchModel searchModel = new SearchModel(true);
        List<MySqlConfiguration> mySqlConfigurations = DatabaseHelper.findAll(MySqlConfiguration.class, searchModel);
        Map<String, List<MySqlConfiguration>> mySqlConfigurationMap = new HashMap<String, List<MySqlConfiguration>>();
        for (MySqlConfiguration mySqlConfiguration : mySqlConfigurations) {
            String type = mySqlConfiguration.getType();
            List<MySqlConfiguration> mySqlConfigurationList = mySqlConfigurationMap.get(type);
            if (CollectionUtils.isEmpty(mySqlConfigurationList)) {
                mySqlConfigurationList = new ArrayList<MySqlConfiguration>();
                mySqlConfigurationMap.put(type, mySqlConfigurationList);
            }
            mySqlConfigurationList.add(mySqlConfiguration);
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, List<MySqlConfiguration>> entry : mySqlConfigurationMap.entrySet()) {
            stringBuilder.append("[").append(entry.getKey()).append("]").append("\n");
            List<MySqlConfiguration> mySqlConfigurationList = entry.getValue();
            for (MySqlConfiguration mySqlConfiguration : mySqlConfigurationList) {
                stringBuilder.append(mySqlConfiguration.getName()).append("=").append(mySqlConfiguration.getValue()).append("\n");
            }
        }
        return ApiRest.builder().data(stringBuilder.toString()).message("处理成功！").successful(true).build();
    }
}
