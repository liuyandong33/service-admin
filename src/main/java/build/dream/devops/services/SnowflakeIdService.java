package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.devops.SnowflakeIdConfiguration;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.devops.models.snowflakeid.SaveSnowflakeIdConfigurationModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class SnowflakeIdService {
    /**
     * 保存雪花算法配置
     *
     * @param saveSnowflakeIdConfigurationModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveSnowflakeIdConfiguration(SaveSnowflakeIdConfigurationModel saveSnowflakeIdConfigurationModel) {
        Long userId = saveSnowflakeIdConfigurationModel.obtainUserId();
        Integer workerId = saveSnowflakeIdConfigurationModel.getWorkerId();
        Integer dataCenterId = saveSnowflakeIdConfigurationModel.getDataCenterId();
        String ipAddress = saveSnowflakeIdConfigurationModel.getIpAddress();
        String applicationName = saveSnowflakeIdConfigurationModel.getApplicationName();

        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(SnowflakeIdConfiguration.ColumnName.IP_ADDRESS, ipAddress)
                .equal(SnowflakeIdConfiguration.ColumnName.APPLICATION_NAME, applicationName)
                .build();
        SnowflakeIdConfiguration snowflakeIdConfiguration = DatabaseHelper.find(SnowflakeIdConfiguration.class, searchModel);
        if (Objects.isNull(snowflakeIdConfiguration)) {
            snowflakeIdConfiguration = SnowflakeIdConfiguration.builder()
                    .workerId(workerId)
                    .dataCenterId(dataCenterId)
                    .ipAddress(ipAddress)
                    .applicationName(applicationName)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增雪花算法配置！")
                    .build();
            DatabaseHelper.insert(snowflakeIdConfiguration);
        } else {
            snowflakeIdConfiguration.setWorkerId(workerId);
            snowflakeIdConfiguration.setDataCenterId(dataCenterId);
            snowflakeIdConfiguration.setUpdatedUserId(userId);
            DatabaseHelper.update(snowflakeIdConfiguration);
        }
        return ApiRest.builder().data(snowflakeIdConfiguration).message("保存雪花算法配置成功！").successful(true).build();
    }
}
