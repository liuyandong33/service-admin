package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.domains.admin.Host;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.SearchModel;
import build.dream.devops.mappers.HostMapper;
import build.dream.devops.models.host.ListHostsModel;
import build.dream.devops.models.host.SaveHostModel;
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
        int memorySize = saveHostModel.getMemorySize();

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
}
