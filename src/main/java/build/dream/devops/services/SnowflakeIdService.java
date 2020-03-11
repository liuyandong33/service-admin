package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.devops.SnowflakeIdConfiguration;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.UpdateModel;
import build.dream.devops.models.snowflakeid.ListSnowflakeIdConfigurationsModel;
import build.dream.devops.models.snowflakeid.SaveSnowflakeIdConfigurationModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        Integer workerId = saveSnowflakeIdConfigurationModel.getWorkerId();
        Integer dataCenterId = saveSnowflakeIdConfigurationModel.getDataCenterId();
        String ipAddress = saveSnowflakeIdConfigurationModel.getIpAddress();
        String applicationName = saveSnowflakeIdConfigurationModel.getApplicationName();
        String description = saveSnowflakeIdConfigurationModel.getDescription();

        SearchModel searchModel = SearchModel.builder()
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
                    .description(description)
                    .build();
            DatabaseHelper.insert(snowflakeIdConfiguration);
        } else {
            UpdateModel updateModel = UpdateModel.builder()
                    .addContentValue(SnowflakeIdConfiguration.ColumnName.WORKER_ID, workerId, 1)
                    .addContentValue(SnowflakeIdConfiguration.ColumnName.DATA_CENTER_ID, dataCenterId, 1)
                    .addContentValue(SnowflakeIdConfiguration.ColumnName.DESCRIPTION, description, 1)
                    .equal(SnowflakeIdConfiguration.ColumnName.IP_ADDRESS, ipAddress)
                    .equal(SnowflakeIdConfiguration.ColumnName.APPLICATION_NAME, applicationName)
                    .build();
            DatabaseHelper.universalUpdate(updateModel, SnowflakeIdConfiguration.TABLE_NAME);
        }
        return ApiRest.builder().message("保存雪花算法配置成功！").successful(true).build();
    }

    /**
     * 分页查询雪花算法配置
     *
     * @param listSnowflakeIdConfigurationsModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listSnowflakeIdConfigurations(ListSnowflakeIdConfigurationsModel listSnowflakeIdConfigurationsModel) {
        Integer page = listSnowflakeIdConfigurationsModel.getPage();
        Integer rows = listSnowflakeIdConfigurationsModel.getRows();

        SearchModel searchModel = SearchModel.builder().build();
        Long total = DatabaseHelper.count(SnowflakeIdConfiguration.class, searchModel);
        List<SnowflakeIdConfiguration> snowflakeIdConfigurations = null;
        if (total > 0) {
            PagedSearchModel pagedSearchModel = PagedSearchModel.builder()
                    .page(page)
                    .rows(rows)
                    .build();
            snowflakeIdConfigurations = DatabaseHelper.findAllPaged(SnowflakeIdConfiguration.class, pagedSearchModel);
        } else {
            snowflakeIdConfigurations = new ArrayList<SnowflakeIdConfiguration>();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", total);
        data.put("rows", snowflakeIdConfigurations);
        return ApiRest.builder().data(data).message("查询雪花算法配置成功！").successful(true).build();
    }
}
