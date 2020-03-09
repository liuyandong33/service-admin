package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.admin.$Service;
import build.dream.common.domains.admin.App;
import build.dream.common.domains.admin.JavaOperation;
import build.dream.common.domains.admin.ServiceConfiguration;
import build.dream.common.utils.*;
import build.dream.devops.constants.Constants;
import build.dream.devops.mappers.ServiceMapper;
import build.dream.devops.models.service.DeployModel;
import build.dream.devops.models.service.ListServicesModel;
import build.dream.devops.models.service.ObtainServiceInfoModel;
import build.dream.devops.models.service.SaveServiceModel;
import build.dream.devops.utils.JSchUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.io.OutputStream;
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
        data.put("configurations", serviceConfigurations);
        data.put("nodes", serviceNodes);
        return ApiRest.builder().data(data).message("获取服务信息成功！").successful(true).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveService(SaveServiceModel saveServiceModel) {
        Long userId = saveServiceModel.obtainUserId();
        Long id = saveServiceModel.getId();
        Long appId = saveServiceModel.getAppId();
        String name = saveServiceModel.getName();
        String programName = saveServiceModel.getProgramName();
        String programVersion = saveServiceModel.getProgramVersion();
        String healthCheckPath = saveServiceModel.getHealthCheckPath();
        Map<String, String> configurations = saveServiceModel.getConfigurations();
        Map<String, Object> javaOpts = saveServiceModel.getJavaOpts();

        $Service service = null;
        if (Objects.nonNull(id)) {
            SearchModel searchModel = SearchModel.builder()
                    .autoSetDeletedFalse()
                    .equal(App.ColumnName.ID, id)
                    .build();
            service = DatabaseHelper.find($Service.class, searchModel);
            ValidateUtils.notNull(service, "服务不存在！");

            service.setName(name);
            service.setProgramName(programName);
            service.setProgramVersion(programVersion);
            service.setHealthCheckPath(healthCheckPath);
            service.setUpdatedUserId(userId);
            service.setUpdatedRemark("修改服务信息！");
            DatabaseHelper.update(service);
        } else {
            service = $Service.builder()
                    .appId(appId)
                    .name(name)
                    .programName(programName)
                    .programVersion(programVersion)
                    .healthCheckPath(healthCheckPath)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增服务信息！")
                    .build();
            DatabaseHelper.insert(service);
        }
        Long serviceId = service.getId();

        serviceMapper.deleteJavaOperations(serviceId);
        JavaOperation javaOperation = JacksonUtils.readValue(JacksonUtils.writeValueAsString(javaOpts), JavaOperation.class);
        javaOperation.setServiceId(serviceId);
        javaOperation.setCreatedUserId(userId);
        javaOperation.setUpdatedUserId(userId);
        javaOperation.setUpdatedRemark("设置JVM属性！");
        DatabaseHelper.insert(javaOperation);

        serviceMapper.deleteServiceConfigurations(serviceId);

        if (MapUtils.isNotEmpty(configurations)) {
            List<ServiceConfiguration> serviceConfigurations = new ArrayList<ServiceConfiguration>();
            for (Map.Entry<String, String> entry : configurations.entrySet()) {
                ServiceConfiguration serviceConfiguration = ServiceConfiguration.builder()
                        .serviceId(serviceId)
                        .configurationKey(entry.getKey())
                        .configurationValue(entry.getValue())
                        .createdUserId(userId)
                        .updatedUserId(userId)
                        .updatedRemark("设置配置！")
                        .build();
                serviceConfigurations.add(serviceConfiguration);
            }
            DatabaseHelper.insertAll(serviceConfigurations);
        }
        return ApiRest.builder().message("保存服务成功！").successful(true).build();
    }

    public ApiRest deploy(DeployModel deployModel) {
        Long serviceId = deployModel.getServiceId();

        $Service service = DatabaseHelper.find($Service.class, serviceId);
        ValidateUtils.notNull(service, "服务不存在！");

        SearchModel javaOperationSearchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(JavaOperation.ColumnName.SERVICE_ID, serviceId)
                .build();
        JavaOperation javaOperation = DatabaseHelper.find(JavaOperation.class, javaOperationSearchModel);

        SearchModel serviceConfigurationSearchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .equal(ServiceConfiguration.ColumnName.SERVICE_ID, serviceId)
                .build();
        List<ServiceConfiguration> serviceConfigurations = DatabaseHelper.findAll(ServiceConfiguration.class, serviceConfigurationSearchModel);

        StringBuilder command = new StringBuilder("nohup java");
        if (Objects.nonNull(javaOperation)) {
            command.append(" ");
            command.append(javaOperation.buildJavaOpts());
        }
        command.append(" -jar ").append(service.getProgramName()).append(".jar");
        if (CollectionUtils.isNotEmpty(serviceConfigurations)) {
            for (ServiceConfiguration serviceConfiguration : serviceConfigurations) {
                command.append(" --");
                command.append(serviceConfiguration.getConfigurationKey());
                command.append("=");
                command.append(serviceConfiguration.getConfigurationValue());
            }
        }
        command.append(" &");

        List<Map<String, Object>> serviceNodes = serviceMapper.listServiceNodes(serviceId);

        Session session = null;
        ChannelSftp channelSftp = null;
        List<Session> nodeSessions = null;
        List<ChannelSftp> nodeChannelSftps = null;
        try {
            session = JSchUtils.createSession("root", "root", "192.168.1.10", 22);
            channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
            channelSftp.connect();
            InputStream inputStream = channelSftp.get("/usr/local/development/snapshots/" + service.getProgramName() + "/" + service.getProgramName() + "-" + service.getProgramVersion() + ".jar");

            nodeSessions = new ArrayList<Session>();
            nodeChannelSftps = new ArrayList<ChannelSftp>();
            for (Map<String, Object> serviceNode : serviceNodes) {
                String userName = MapUtils.getString(serviceNode, "userName");
                String password = MapUtils.getString(serviceNode, "password");
                String ipAddress = MapUtils.getString(serviceNode, "ipAddress");
                int sshPort = MapUtils.getIntValue(serviceNode, "sshPort");
                Session nodeSession = JSchUtils.createSession(userName, password, ipAddress, sshPort);
                ChannelSftp nodeChannelSftp = (ChannelSftp) JSchUtils.openChannel(nodeSession, Constants.CHANNEL_TYPE_SFTP);
                nodeChannelSftp.connect();
                nodeSessions.add(nodeSession);
                nodeChannelSftps.add(nodeChannelSftp);
            }

            for (Session nodeSession : nodeSessions) {
                if (!JSchUtils.exists(nodeSession, "/usr/local/development/services/")) {
                    JSchUtils.mkdirs(nodeSession, "/usr/local/development/services/");
                }

                if (JSchUtils.exists(nodeSession, "/usr/local/development/services/" + service.getProgramName() + ".jar")) {
                    JSchUtils.delete(nodeSession, "/usr/local/development/services/" + service.getProgramName() + ".jar");
                }
            }

            List<OutputStream> outputStreams = new ArrayList<OutputStream>();
            for (ChannelSftp nodeChannelSftp : nodeChannelSftps) {
                OutputStream outputStream = nodeChannelSftp.put("/usr/local/development/services/" + service.getProgramName() + ".jar");
                outputStreams.add(outputStream);
            }

            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
                for (OutputStream outputStream : outputStreams) {
                    outputStream.write(buffer, 0, length);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (CollectionUtils.isNotEmpty(nodeChannelSftps)) {
                nodeChannelSftps.forEach(channelSftp1 -> JSchUtils.disconnectChannel(channelSftp1));
            }
            if (CollectionUtils.isNotEmpty(nodeSessions)) {
                nodeSessions.forEach(session1 -> JSchUtils.disconnectSession(session1));
            }
            JSchUtils.disconnectChannel(channelSftp);
            JSchUtils.disconnectSession(session);
        }
        return ApiRest.builder().data(command.toString()).message("部署成功！").successful(true).build();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> findAllServiceNodes() {
        return serviceMapper.findAllServiceNodes();
    }
}
