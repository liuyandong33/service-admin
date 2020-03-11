package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.devops.Host;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.common.utils.ValidateUtils;
import build.dream.devops.constants.Constants;
import build.dream.devops.mappers.HostMapper;
import build.dream.devops.models.host.ListHostsModel;
import build.dream.devops.models.host.SaveHostModel;
import build.dream.devops.models.host.ShutdownModel;
import build.dream.devops.models.host.StartModel;
import build.dream.devops.utils.JSchUtils;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HostService {
    @Autowired
    private HostMapper hostMapper;

    /**
     * 添加主机
     *
     * @param saveHostModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest saveHost(SaveHostModel saveHostModel) {
        Long userId = saveHostModel.obtainUserId();
        Long id = saveHostModel.getId();
        Integer type = saveHostModel.getType();
        Long parentId = saveHostModel.getParentId();
        String name = saveHostModel.getName();
        String ipAddress = saveHostModel.getIpAddress();
        int sshPort = saveHostModel.getSshPort();
        String userName = saveHostModel.getUserName();
        String password = saveHostModel.getPassword();
        int diskSize = saveHostModel.getDiskSize();
        int cpuCoreQuantity = saveHostModel.getCpuCoreQuantity();
        Long memorySize = saveHostModel.getMemorySize();

        Session session = null;
        try {
            session = JSchUtils.createSession(userName, password, ipAddress, sshPort);
        } catch (Exception e) {
            throw new RuntimeException("服务器与宿主机之间无法连通，请检查IP地址、SSH连接端口号、用户名、密码填写是否正确！");
        } finally {
            JSchUtils.disconnectSession(session);
        }

        Host host = null;
        if (id != null) {
            host = DatabaseHelper.find(Host.class, id);
            Validate.notNull(host, "主机不存在！");

            host.setType(type);
            host.setParentId(parentId);
            host.setName(name);
            host.setIpAddress(ipAddress);
            host.setSshPort(sshPort);
            host.setUserName(userName);
            host.setPassword(password);
            host.setDiskSize(diskSize);
            host.setCpuCoreQuantity(cpuCoreQuantity);
            host.setMemorySize(memorySize);
            host.setUpdatedUserId(userId);
            host.setUpdatedRemark("修改主机信息！");
            DatabaseHelper.update(host);
        } else {
            host = Host.builder()
                    .type(type)
                    .parentId(parentId)
                    .name(name)
                    .ipAddress(ipAddress)
                    .sshPort(sshPort)
                    .userName(userName)
                    .password(password)
                    .diskSize(diskSize)
                    .cpuCoreQuantity(cpuCoreQuantity)
                    .memorySize(memorySize)
                    .createdUserId(userId)
                    .updatedUserId(userId)
                    .updatedRemark("新增主机信息")
                    .build();
            DatabaseHelper.insert(host);
        }
        return ApiRest.builder().data(host).message("保存成功！").successful(true).build();
    }

    /**
     * 查询主机列表
     *
     * @param listHostsModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest listHosts(ListHostsModel listHostsModel) {
        int page = listHostsModel.getPage();
        int rows = listHostsModel.getRows();

        Long total = hostMapper.countHosts();
        List<Map<String, Object>> hosts = null;
        if (total > 0) {
            hosts = hostMapper.listHosts((page - 1) * rows, rows);
        } else {
            hosts = new ArrayList<Map<String, Object>>();
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", total);
        data.put("rows", hosts);
        return ApiRest.builder().data(data).message("查询主机列表成功！").successful(true).build();
    }

    @Transactional(readOnly = true)
    public List<Host> obtainAllHosts() {
        return DatabaseHelper.findAll(Host.class, SearchModel.builder().autoSetDeletedFalse().build());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateHost(Host host) {
        DatabaseHelper.update(host);
    }

    /**
     * 关闭虚拟机
     *
     * @param shutdownModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest shutdown(ShutdownModel shutdownModel) {
        Long userId = shutdownModel.obtainUserId();
        Long hostId = shutdownModel.getHostId();
        Host host = DatabaseHelper.find(Host.class, hostId);
        ValidateUtils.notNull(host, "虚拟机不存在！");
        ValidateUtils.isTrue(host.getType() == Constants.HOST_TYPE_VIRTUAL_MACHINE, "只能关闭虚拟机！");
        ValidateUtils.isTrue(host.getStatus() == Constants.HOST_STATUS_RUNNING, "虚拟机未运行，不能进行关机操作！");

        Host parentHost = DatabaseHelper.find(Host.class, host.getParentId());
        Session session = null;
        try {
            session = JSchUtils.createSession(parentHost.getUserName(), parentHost.getPassword(), parentHost.getIpAddress(), parentHost.getSshPort());
            String result = JSchUtils.executeCommand(session, "virsh shutdown " + host.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JSchUtils.disconnectSession(session);
        }

        host.setStatus(Constants.HOST_STATUS_STOPPED);
        host.setUpdatedUserId(userId);
        host.setUpdatedRemark("关闭虚拟机！");
        DatabaseHelper.update(host);
        return ApiRest.builder().message("关闭虚拟机成功！").build();
    }

    /**
     * 开启虚拟机
     *
     * @param startModel
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest start(StartModel startModel) {
        Long userId = startModel.obtainUserId();
        Long hostId = startModel.getHostId();
        Host host = DatabaseHelper.find(Host.class, hostId);
        ValidateUtils.notNull(host, "虚拟机不存在！");
        ValidateUtils.isTrue(host.getType() == Constants.HOST_TYPE_VIRTUAL_MACHINE, "只能关闭虚拟机！");
        ValidateUtils.isTrue(host.getStatus() == Constants.HOST_STATUS_STOPPED, "虚拟机正在运行中，不能进行开机操作！");

        Host parentHost = DatabaseHelper.find(Host.class, host.getParentId());
        Session session = null;
        try {
            session = JSchUtils.createSession(parentHost.getUserName(), parentHost.getPassword(), parentHost.getIpAddress(), parentHost.getSshPort());
            String result = JSchUtils.executeCommand(session, "virsh start " + host.getName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JSchUtils.disconnectSession(session);
        }

        host.setStatus(Constants.HOST_STATUS_RUNNING);
        host.setUpdatedUserId(userId);
        host.setUpdatedRemark("开启虚拟机！");
        DatabaseHelper.update(host);
        return ApiRest.builder().message("开启虚拟机成功！").build();
    }
}
