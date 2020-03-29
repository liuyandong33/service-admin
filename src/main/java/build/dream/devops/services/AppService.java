package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.devops.App;
import build.dream.common.utils.*;
import build.dream.devops.constants.Constants;
import build.dream.devops.models.app.ListAppsModel;
import build.dream.devops.models.app.SaveAppModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppService {
    /**
     * 分页查询应用
     *
     * @param listAppsModel
     * @return
     */
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

    /**
     * 保存应用
     *
     * @param saveAppModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveApp(SaveAppModel saveAppModel) {
        Long userId = saveAppModel.obtainUserId();
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
            app.setCreatedUserId(userId);
            app.setUpdatedUserId(userId);
            app.setUpdatedRemark("修改应用信息！");
        } else {
            App app = App.builder()
                    .name(name)
                    .description(description)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增应用信息！")
                    .build();
            DatabaseHelper.insert(app);
        }
        return ApiRest.builder().message("保存应用成功！").successful(true).build();
    }
}
