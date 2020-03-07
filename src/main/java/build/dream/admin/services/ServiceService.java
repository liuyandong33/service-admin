package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.mappers.ServiceMapper;
import build.dream.admin.models.service.ListServicesModel;
import build.dream.admin.models.service.ObtainServiceInfoModel;
import build.dream.common.api.ApiRest;
import build.dream.common.domains.admin.$Service;
import build.dream.common.domains.admin.JavaOperation;
import build.dream.common.domains.admin.ServiceConfiguration;
import build.dream.common.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ServiceService {
    @Autowired
    private ServiceMapper serviceMapper;

    @Transactional(readOnly = true)
    public ApiRest listServices(ListServicesModel listServicesModel) {
        Integer appId = listServicesModel.getAppId();
        Integer page = listServicesModel.getPage();
        Integer rows = listServicesModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition($Service.ColumnName.DELETED, Constants.SQL_OPERATION_SYMBOL_EQUAL, 0));
        searchConditions.add(new SearchCondition($Service.ColumnName.APP_ID, Constants.SQL_OPERATION_SYMBOL_EQUAL, appId));
        SearchModel searchModel = SearchModel.builder()
                .searchConditions(searchConditions)
                .build();
        Long total = DatabaseHelper.count($Service.class, searchModel);
        List<$Service> services = null;
        if (total > 0) {
            PagedSearchModel pagedSearchModel = PagedSearchModel.builder()
                    .searchConditions(searchConditions)
                    .page(page)
                    .rows(rows)
                    .build();
            services = DatabaseHelper.findAllPaged($Service.class, pagedSearchModel);
        } else {
            services = new ArrayList<$Service>();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", total);
        data.put("rows", services);
        return ApiRest.builder().data(data).message("查询服务列表成功").successful(true).build();
    }

    @Transactional(readOnly = true)
    public ApiRest obtainServiceInfo(ObtainServiceInfoModel obtainServiceInfoModel) {
        Long serviceId = obtainServiceInfoModel.getServiceId();
        $Service service = DatabaseHelper.find($Service.class, serviceId);
        ValidateUtils.notNull(service, "服务不存在！");

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("service", service);
        SearchModel javaOperationSearchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(JavaOperation.ColumnName.SERVICE_ID, serviceId)
                .build();
        JavaOperation javaOperation = DatabaseHelper.find(JavaOperation.class, javaOperationSearchModel);
        if (Objects.nonNull(javaOperation)) {
            data.put("javaOpts", javaOperation.buildJavaOpts());
        }

        SearchModel serviceConfigurationSearchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(ServiceConfiguration.ColumnName.SERVICE_ID, serviceId)
                .build();
        List<ServiceConfiguration> serviceConfigurations = DatabaseHelper.findAll(ServiceConfiguration.class, serviceConfigurationSearchModel);
        List<Map<String, Object>> serviceNodes = serviceMapper.listServiceNodes(serviceId);
        data.put("serviceConfigurations", serviceConfigurations);
        data.put("serviceNodes", serviceNodes);
        return ApiRest.builder().data(data).message("获取服务信息成功！").successful(true).build();
    }
}
