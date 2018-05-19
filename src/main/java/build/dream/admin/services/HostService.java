package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.host.*;
import build.dream.admin.utils.DatabaseHelper;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.admin.domains.Host;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.SearchModel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Service
public class HostService {
    /**
     * 添加主机
     *
     * @param saveModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest save(SaveModel saveModel) {
        BigInteger id = saveModel.getId();
        BigInteger userId = saveModel.getUserId();
        String name = saveModel.getName();
        String ipAddress = saveModel.getIpAddress();
        int sshPort = saveModel.getSshPort();
        String userName = saveModel.getUserName();
        String password = saveModel.getPassword();

        try {
            Session session = JSchUtils.createSession(userName, password, ipAddress, sshPort);
            JSchUtils.disconnectSession(session);
        } catch (Exception e) {
            throw new RuntimeException("服务器与宿主机之间无法连通，请检查IP地址、SSH连接端口号、用户名、密码填写是否正确！");
        }

        Host host = null;
        if (id != null) {
            SearchModel searchModel = new SearchModel(true);
            searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, id);
            host = DatabaseHelper.find(Host.class, searchModel);
            Validate.notNull(host, "主机不存在！");

            host.setName(name);
            host.setIpAddress(ipAddress);
            host.setSshPort(sshPort);
            host.setUserName(userName);
            host.setPassword(password);
            host.setLastUpdateUserId(userId);
            DatabaseHelper.update(host);
        } else {
            host = new Host();
            host.setType(1);
            host.setName(name);
            host.setIpAddress(ipAddress);
            host.setSshPort(sshPort);
            host.setUserName(userName);
            host.setPassword(password);
            host.setCreateUserId(userId);
            host.setLastUpdateUserId(userId);
            DatabaseHelper.insert(host);
        }
        return new ApiRest(host, "保存成功！");
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest createHost(CreateHostModel createHostModel) throws JSchException, IOException {
        BigInteger hostId = createHostModel.getHostId();
        String name = createHostModel.getName();
        int diskSize = createHostModel.getDiskSize();
        String password = createHostModel.getPassword();
        int cupCoreQuantity = createHostModel.getCupCoreQuantity();
        int memorySize = createHostModel.getMemorySize();
        BigInteger userId = createHostModel.getUserId();

        SearchModel searchModel = new SearchModel(true);
        searchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, hostId);
        Host host = DatabaseHelper.find(Host.class, searchModel);
        Validate.notNull(host, "主机不存在！");

        Session session = JSchUtils.createSession(host.getUserName(), host.getPassword(), host.getIpAddress(), host.getSshPort());

        String diskPath = File.separator + "usr" + File.separator + "local" + File.separator + "kvm" + File.separator + name + ".raw";
        StringBuilder createDiskCommand = new StringBuilder("qemu-img create");
        createDiskCommand.append(" -f raw");
        createDiskCommand.append(" " + diskPath);
        createDiskCommand.append(" " + diskSize + "G");
        String createDiskResult = JSchUtils.executeCommand(session, createDiskCommand.toString());
        System.out.println(createDiskResult);

        String iosPath = "/disk/iso/CentOS-7-x86_64-Everything-1804.iso";
        StringBuilder installCommand = new StringBuilder("virt-install --virt-type kvm");
        installCommand.append(" --name " + name);
        installCommand.append(" --ram " + memorySize);
        installCommand.append(" --cdrom=" + iosPath);
        installCommand.append(" --disk path=" + diskPath);
        installCommand.append(" --network network=default");
        installCommand.append(" --graphics vnc,listen=0.0.0.0");
        installCommand.append(" --noautoconsole");
        String installResult = JSchUtils.executeCommand(session, installCommand.toString());
        System.out.println(installResult);

        Host childHost = new Host();
        childHost.setType(2);
        childHost.setParentId(hostId);
        childHost.setName(name);
        childHost.setIpAddress("");
        childHost.setSshPort(22);
        childHost.setUserName("root");
        childHost.setPassword(password);
        childHost.setCreateUserId(userId);
        childHost.setLastUpdateUserId(userId);
        DatabaseHelper.insert(childHost);

        return new ApiRest(childHost, "虚拟主机创建成功！");
    }

    /**
     * 查询主机列表
     *
     * @param listModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest list(ListModel listModel) {
        return new ApiRest();
    }

    /**
     * 开机
     * @param startModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest start(StartModel startModel) throws JSchException, IOException {
        BigInteger hostId = startModel.getHostId();
        BigInteger userId = startModel.getUserId();

        SearchModel childHostSearchModel = new SearchModel(true);
        childHostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, hostId);
        Host childHost = DatabaseHelper.find(Host.class, childHostSearchModel);
        Validate.notNull(childHost, "主机不存在！");

        SearchModel hostSearchModel = new SearchModel(true);
        hostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, childHost.getParentId());
        Host host = DatabaseHelper.find(Host.class, hostSearchModel);
        Validate.notNull(host, "宿主机不存在！");

        Session session = JSchUtils.createSession(host.getUserName(), host.getPassword(), host.getIpAddress(), host.getSshPort());
        String startCommand = "virsh start " + childHost.getName();
        String startResult = JSchUtils.executeCommand(session, startCommand);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("开机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 关机
     * @param shutdownModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest shutdown(ShutdownModel shutdownModel) throws JSchException, IOException {
        BigInteger hostId = shutdownModel.getHostId();
        BigInteger userId = shutdownModel.getUserId();

        SearchModel childHostSearchModel = new SearchModel(true);
        childHostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, hostId);
        Host childHost = DatabaseHelper.find(Host.class, childHostSearchModel);
        Validate.notNull(childHost, "主机不存在！");

        SearchModel hostSearchModel = new SearchModel(true);
        hostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, childHost.getParentId());
        Host host = DatabaseHelper.find(Host.class, hostSearchModel);
        Validate.notNull(host, "宿主机不存在！");

        Session session = JSchUtils.createSession(host.getUserName(), host.getPassword(), host.getIpAddress(), host.getSshPort());
        String shutdownCommand = "virsh shutdown " + childHost.getName();
        String shutdownResult = JSchUtils.executeCommand(session, shutdownCommand);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("关机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 关机
     * @param destroyModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest destroy(DestroyModel destroyModel) throws JSchException, IOException {
        BigInteger hostId = destroyModel.getHostId();
        BigInteger userId = destroyModel.getUserId();

        SearchModel childHostSearchModel = new SearchModel(true);
        childHostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, hostId);
        Host childHost = DatabaseHelper.find(Host.class, childHostSearchModel);
        Validate.notNull(childHost, "主机不存在！");

        SearchModel hostSearchModel = new SearchModel(true);
        hostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, childHost.getParentId());
        Host host = DatabaseHelper.find(Host.class, hostSearchModel);
        Validate.notNull(host, "宿主机不存在！");

        Session session = JSchUtils.createSession(host.getUserName(), host.getPassword(), host.getIpAddress(), host.getSshPort());
        String shutdownCommand = "virsh destroy " + childHost.getName();
        String shutdownResult = JSchUtils.executeCommand(session, shutdownCommand);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("关机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }

    /**
     * 删除虚拟机
     * @param undefineModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest undefine(UndefineModel undefineModel) throws JSchException, IOException {
        BigInteger hostId = undefineModel.getHostId();
        BigInteger userId = undefineModel.getUserId();

        SearchModel childHostSearchModel = new SearchModel(true);
        childHostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, hostId);
        Host childHost = DatabaseHelper.find(Host.class, childHostSearchModel);
        Validate.notNull(childHost, "主机不存在！");

        SearchModel hostSearchModel = new SearchModel(true);
        hostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, childHost.getParentId());
        Host host = DatabaseHelper.find(Host.class, hostSearchModel);
        Validate.notNull(host, "宿主机不存在！");

        Session session = JSchUtils.createSession(host.getUserName(), host.getPassword(), host.getIpAddress(), host.getSshPort());
        String shutdownCommand = "virsh undefine " + childHost.getName();
        String shutdownResult = JSchUtils.executeCommand(session, shutdownCommand);

        childHost.setDeleted(true);
        DatabaseHelper.update(childHost);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("删除虚拟机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
