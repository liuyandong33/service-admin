package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import build.dream.devops.constants.Constants;
import build.dream.devops.domains.LoggingEvent;
import build.dream.devops.models.log.ListLogsModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LogService {
    @Transactional(readOnly = true)
    public ApiRest listLogs(ListLogsModel listLogsModel) {
        String deploymentEnvironment = listLogsModel.getDeploymentEnvironment();
        String partitionCode = listLogsModel.getPartitionCode();
        String serviceName = listLogsModel.getServiceName();
        Date startTime = listLogsModel.getStartTime();
        Date endTime = listLogsModel.getEndTime();
        String levelString = listLogsModel.getLevelString();
        String searchString = listLogsModel.getSearchString();
        Integer page = listLogsModel.getPage();
        Integer rows = listLogsModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        if (Objects.nonNull(startTime)) {
            searchConditions.add(new SearchCondition("timestmp", Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUAL, startTime.getTime()));
        }
        if (Objects.nonNull(endTime)) {
            searchConditions.add(new SearchCondition("timestmp", Constants.SQL_OPERATION_SYMBOL_LESS_THAN_EQUAL, endTime.getTime()));
        }
        if (StringUtils.isNotBlank(levelString)) {
            searchConditions.add(new SearchCondition("level_string", Constants.SQL_OPERATION_SYMBOL_EQUAL, levelString));
        }
        if (StringUtils.isNotBlank(searchString)) {
            searchConditions.add(new SearchCondition("formatted_message", Constants.SQL_OPERATION_SYMBOL_LIKE, "%" + searchString + "%"));
        }
        SearchModel searchModel = SearchModel.builder().searchConditions(searchConditions).build();
        long total = DatabaseHelper.count(LoggingEvent.class, searchModel);
        List<LoggingEvent> loggingEvents = null;
        if (total > 0) {
            PagedSearchModel pagedSearchModel = PagedSearchModel.builder()
                    .searchConditions(searchConditions)
                    .page(page)
                    .rows(rows)
                    .orderBy("timestmp DESC, event_id DESC")
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
