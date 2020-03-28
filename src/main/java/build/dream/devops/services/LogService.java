package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import build.dream.devops.domains.LoggingEvent;
import build.dream.devops.models.log.ListLogsModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogService {
    @Transactional(readOnly = true)
    public ApiRest listLogs(ListLogsModel listLogsModel) {
        String deploymentEnvironment = listLogsModel.getDeploymentEnvironment();
        String partitionCode = listLogsModel.getPartitionCode();
        String serviceName = listLogsModel.getServiceName();
        Integer page = listLogsModel.getPage();
        Integer rows = listLogsModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        SearchModel searchModel = SearchModel.builder().searchConditions(searchConditions).build();
        long total = DatabaseHelper.count(LoggingEvent.class, searchModel);
        List<LoggingEvent> loggingEvents = null;
        if (total > 0) {
            PagedSearchModel pagedSearchModel = PagedSearchModel.builder()
                    .searchConditions(searchConditions)
                    .page(page)
                    .rows(rows)
                    .build();
            loggingEvents = DatabaseHelper.findAllPaged(LoggingEvent.class, pagedSearchModel);
        } else {
            loggingEvents = new ArrayList<LoggingEvent>();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", total);
        data.put("rows", loggingEvents);
        return ApiRest.builder().data(data).message("查询日志成功！").successful(true).build();
    }
}
