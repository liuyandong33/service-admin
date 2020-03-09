package build.dream.devops.services;

import build.dream.devops.constants.Constants;
import build.dream.devops.models.host.*;
import build.dream.devops.utils.JSchUtils;
import build.dream.devops.utils.VirtualMachineUtils;
import build.dream.common.api.ApiRest;
import build.dream.common.domains.admin.Host;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.PagedSearchModel;
import build.dream.common.utils.SearchCondition;
import build.dream.common.utils.SearchModel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.apache.commons.lang.Validate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Long id = saveModel.getId();
        Long userId = saveModel.getUserId();
        String name = saveModel.getName();
        String ipAddress = saveModel.getIpAddress();
        int sshPort = saveModel.getSshPort();
        String userName = saveModel.getUserName();
        String password = saveModel.getPassword();
        int diskSize = saveModel.getDiskSize();
        int cpuCoreQuantity = saveModel.getCpuCoreQuantity();
        int memorySize = saveModel.getMemorySize();

        try {
            Session session = JSchUtils.createSession(userName, password, ipAddress, sshPort);
            JSchUtils.disconnectSession(session);
        } catch (Exception e) {
            throw new RuntimeException("服务器与宿主机之间无法连通，请检查IP地址、SSH连接端口号、用户名、密码填写是否正确！");
        }

        Host host = null;
        if (id != null) {
            host = DatabaseHelper.find(Host.class, id);
            Validate.notNull(host, "主机不存在！");

            host.setName(name);
            host.setIpAddress(ipAddress);
            host.setSshPort(sshPort);
            host.setUserName(userName);
            host.setPassword(password);
            host.setUpdatedUserId(userId);
            host.setDiskSize(diskSize);
            host.setCpuCoreQuantity(cpuCoreQuantity);
            host.setMemorySize(memorySize);
            DatabaseHelper.update(host);
        } else {
            host = new Host();
            host.setType(1);
            host.setName(name);
            host.setIpAddress(ipAddress);
            host.setSshPort(sshPort);
            host.setUserName(userName);
            host.setPassword(password);
            host.setCreatedUserId(userId);
            host.setUpdatedUserId(userId);
            host.setDiskSize(diskSize);
            host.setCpuCoreQuantity(cpuCoreQuantity);
            host.setMemorySize(memorySize);
            DatabaseHelper.insert(host);
        }
        return ApiRest.builder().data(host).message("保存成功！").successful(true).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest createHost(CreateHostModel createHostModel) throws JSchException, IOException {
        Long hostId = createHostModel.getHostId();
        String name = createHostModel.getName();
        int diskSize = createHostModel.getDiskSize();
        String password = createHostModel.getPassword();
        int cpuCoreQuantity = createHostModel.getCpuCoreQuantity();
        int memorySize = createHostModel.getMemorySize();
        Long userId = createHostModel.getUserId();

        Host host = DatabaseHelper.find(Host.class, hostId);
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
        installCommand.append(" --vcpus=" + cpuCoreQuantity);
        installCommand.append(" --ram " + memorySize);
        installCommand.append(" --cdrom=" + iosPath);
        installCommand.append(" --disk path=" + diskPath);
        installCommand.append(" --network network=default");
        installCommand.append(" --graphics vnc,listen=0.0.0.0");
        installCommand.append(" --noautoconsole");
        String installResult = JSchUtils.executeCommand(session, installCommand.toString());
        System.out.println(installResult);

        JSchUtils.disconnectSession(session);

        Host childHost = new Host();
        childHost.setType(2);
        childHost.setParentId(hostId);
        childHost.setName(name);
        childHost.setIpAddress("");
        childHost.setSshPort(22);
        childHost.setUserName("root");
        childHost.setPassword(password);
        childHost.setDiskSize(diskSize);
        childHost.setCpuCoreQuantity(cpuCoreQuantity);
        childHost.setMemorySize(memorySize);
        childHost.setCreatedUserId(userId);
        childHost.setUpdatedUserId(userId);
        DatabaseHelper.insert(childHost);

        return ApiRest.builder().data(childHost).message("虚拟主机创建成功！").successful(true).build();
    }

    /**
     * 查询主机列表
     *
     * @param listModel
     * @return
     */
    @Transactional(readOnly = true)
    public ApiRest list(ListModel listModel) {
        int type = listModel.getType();
        int page = listModel.getPage();
        int rows = listModel.getRows();
        Long hostId = listModel.getHostId();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition("type", Constants.SQL_OPERATION_SYMBOL_EQUAL, type));
        if (type == 2) {
            searchConditions.add(new SearchCondition("parent_id", Constants.SQL_OPERATION_SYMBOL_EQUAL, hostId));
        }
        SearchModel searchModel = new SearchModel(true);
        searchModel.setSearchConditions(searchConditions);
        long count = DatabaseHelper.count(Host.class, searchModel);

        List<Host> hosts = new ArrayList<Host>();
        if (count > 0) {
            PagedSearchModel pagedSearchModel = new PagedSearchModel(true);
            pagedSearchModel.setSearchConditions(searchConditions);
            pagedSearchModel.setPage(page);
            pagedSearchModel.setRows(rows);
            hosts = DatabaseHelper.findAllPaged(Host.class, pagedSearchModel);
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("total", count);
        data.put("rows", hosts);

        return ApiRest.builder().data(hosts).message("查询主机列表成功！").successful(true).build();
    }

    /**
     * 开机
     *
     * @param startModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest start(StartModel startModel) throws JSchException, IOException {
        Long hostId = startModel.getHostId();
        Long userId = startModel.getUserId();

        String result = VirtualMachineUtils.operate(hostId, userId, Constants.OPERATE_TYPE_START);

        return ApiRest.builder().data(result).message("开机成功！").successful(true).build();
    }

    /**
     * 关机
     *
     * @param shutdownModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest shutdown(ShutdownModel shutdownModel) throws JSchException, IOException {
        Long hostId = shutdownModel.getHostId();
        Long userId = shutdownModel.getUserId();

        String result = VirtualMachineUtils.operate(hostId, userId, Constants.OPERATE_TYPE_SHUTDOWN);

        return ApiRest.builder().data(result).message("关机成功！").successful(true).build();
    }

    /**
     * 关机
     *
     * @param destroyModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest destroy(DestroyModel destroyModel) throws JSchException, IOException {
        Long hostId = destroyModel.getHostId();
        Long userId = destroyModel.getUserId();

        String result = VirtualMachineUtils.operate(hostId, userId, Constants.OPERATE_TYPE_DESTROY);

        return ApiRest.builder().data(result).message("关机成功！").successful(true).build();
    }

    /**
     * 删除虚拟机
     *
     * @param undefineModel
     * @return
     * @throws JSchException
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiRest undefine(UndefineModel undefineModel) throws JSchException, IOException {
        Long hostId = undefineModel.getHostId();
        Long userId = undefineModel.getUserId();

        String result = VirtualMachineUtils.operate(hostId, userId, Constants.OPERATE_TYPE_UNDEFINE);

        return ApiRest.builder().data(result).message("删除虚拟机成功！").successful(true).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest reboot(RebootModel rebootModel) throws JSchException, IOException {
        Long hostId = rebootModel.getHostId();
        Long userId = rebootModel.getUserId();

        String result = VirtualMachineUtils.operate(hostId, userId, Constants.OPERATE_TYPE_REBOOT);

        return ApiRest.builder().data(result).message("重启虚拟机成功！").successful(true).build();
    }

    /**
     * 更新主机配置
     *
     * @param updateModel
     * @return
     * @throws JSchException
     * @throws SftpException
     * @throws IOException
     */
    @Transactional(readOnly = true)
    public ApiRest update(UpdateModel updateModel) throws JSchException, SftpException, IOException, DocumentException {
        Long id = updateModel.getId();
        int diskSize = updateModel.getDiskSize();
        int cpuCoreQuantity = updateModel.getCpuCoreQuantity();
        int memorySize = updateModel.getMemorySize();
        Long userId = updateModel.getUserId();

        Host childHost = DatabaseHelper.find(Host.class, id);
        Validate.notNull(childHost, "主机不存在！");

        Host host = DatabaseHelper.find(Host.class, childHost.getParentId());
        Validate.notNull(host, "宿主机不存在！");

        Session session = JSchUtils.createSession(host.getUserName(), host.getPassword(), host.getIpAddress(), host.getSshPort());
        ChannelSftp channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
        channelSftp.connect();

        String configFilePath = "/etc/libvirt/qemu/" + childHost.getName() + ".xml";
        InputStream inputStream = channelSftp.get(configFilePath);

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        Element memoryElement = rootElement.element("memory");
        memoryElement.setText(String.valueOf(memorySize));

        Element currentMemoryElement = rootElement.element("currentMemory");
        currentMemoryElement.setText(String.valueOf(memorySize));

        Element vcpuElement = rootElement.element("vcpu");
        vcpuElement.setText(String.valueOf(cpuCoreQuantity));

        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding(Constants.CHARSET_NAME_UTF_8);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLWriter xmlWriter = new XMLWriter(byteArrayOutputStream, outputFormat);

        xmlWriter.write(document);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        channelSftp.put(byteArrayInputStream, configFilePath, ChannelSftp.OVERWRITE);

        JSchUtils.disconnectChannel(channelSftp);
        JSchUtils.disconnectSession(session);

        childHost.setDiskSize(diskSize);
        childHost.setCpuCoreQuantity(cpuCoreQuantity);
        childHost.setMemorySize(memorySize);
        DatabaseHelper.update(childHost);

        return ApiRest.builder().data(childHost).message("配置修改成功！").successful(true).build();
    }
}
