package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.app.ListAppsModel;
import build.dream.admin.models.app.SaveAppModel;
import build.dream.common.api.ApiRest;
import build.dream.common.domains.admin.App;
import build.dream.common.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppService {
    @Transactional(readOnly = true)
    public ApiRest listApps(ListAppsModel listAppsModel) {
        Integer page = listAppsModel.getPage();
        Integer rows = listAppsModel.getRows();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition(App.ColumnName.DELETED, Constants.SQL_OPERATION_SYMBOL_EQUAL, 0));
        SearchModel searchModel = SearchModel.builder()
                .searchConditions(searchConditions)
                .build();

        Long total = DatabaseHelper.count(App.class, searchModel);

        List<App> apps = null;
        if (total > 0) {
            PagedSearchModel pagedSearchModel = PagedSearchModel.builder()
                    .searchConditions(searchConditions)
                    .page(page)
                    .rows(rows)
                    .build();
            apps = DatabaseHelper.findAllPaged(App.class, pagedSearchModel);
        } else {
            apps = new ArrayList<App>();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", total);
        data.put("rows", apps);
        return ApiRest.builder().data(data).message("查询应用列表成功！").build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveApp(SaveAppModel saveAppModel) {
        Long id = saveAppModel.getId();
        String name = saveAppModel.getName();
        String description = saveAppModel.getDescription();

        if (Objects.nonNull(id)) {
            SearchModel searchModel = SearchModel.builder()
                    .autoSetDeletedFalse()
                    .equal(App.ColumnName.ID, id)
                    .build();
            App app = DatabaseHelper.find(App.class, searchModel);
            ValidateUtils.notNull(app, "应用不存在！");

            app.setName(name);
            app.setDescription(description);
        } else {
            App app = App.builder()
                    .name(name)
                    .description(description)
                    .createdUserId(0L)
                    .updatedUserId(0L)
                    .build();
            DatabaseHelper.insert(app);
        }
        return ApiRest.builder().message("保存应用成功！").successful(true).build();
    }
}
