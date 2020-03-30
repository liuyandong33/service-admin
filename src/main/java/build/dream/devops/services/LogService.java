package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import build.dream.devops.constants.Constants;
import build.dream.devops.domains.LoggingEvent;
import build.dream.devops.models.log.ClearLogsModel;
import build.dream.devops.models.log.ListLogsModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LogService {
    /**
     * 分页查询日志
     *
     * @param listLogsModel
     * @return
     */
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
            searchConditions.add(new SearchCondition(LoggingEvent.ColumnName.TIMESTMP, Constants.SQL_OPERATION_SYMBOL_GREATER_THAN_EQUAL, startTime.getTime()));
        }
        if (Objects.nonNull(endTime)) {
            searchConditions.add(new SearchCondition(LoggingEvent.ColumnName.TIMESTMP, Constants.SQL_OPERATION_SYMBOL_LESS_THAN_EQUAL, endTime.getTime()));
        }
        if (StringUtils.isNotBlank(levelString)) {
            searchConditions.add(new SearchCondition(LoggingEvent.ColumnName.LEVEL_STRING, Constants.SQL_OPERATION_SYMBOL_EQUAL, levelString));
        }
        if (StringUtils.isNotBlank(searchString)) {
            searchConditions.add(new SearchCondition(LoggingEvent.ColumnName.FORMATTED_MESSAGE, Constants.SQL_OPERATION_SYMBOL_LIKE, "%" + searchString + "%"));
        }
        SearchModel searchModel = SearchModel.builder().searchConditions(searchConditions).build();
        long total = DatabaseHelper.count(LoggingEvent.class, searchModel);
        List<LoggingEvent> loggingEvents = null;
        if (total > 0) {
            PagedSearchModel pagedSearchModel = PagedSearchModel.builder()
                    .searchConditions(searchConditions)
                    .page(page)
                    .rows(rows)
                    .orderBy(LoggingEvent.ColumnName.TIMESTMP + " " + Constants.DESC + ", " + LoggingEvent.ColumnName.EVENT_ID + " " + Constants.DESC)
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

    /**
     * 清除日志
     *
     * @param clearLogsModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest clearLogs(ClearLogsModel clearLogsModel) {
        String deploymentEnvironment = clearLogsModel.getDeploymentEnvironment();
        String partitionCode = clearLogsModel.getPartitionCode();
        String serviceName = clearLogsModel.getServiceName();
        DatabaseHelper.truncateTable("logging_event");
        DatabaseHelper.truncateTable("logging_event_property");
        DatabaseHelper.truncateTable("logging_event_exception");
        return ApiRest.builder().message("清除日志成功！").successful(true).build();
    }
}
