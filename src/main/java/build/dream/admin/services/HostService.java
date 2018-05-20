package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.host.*;
import build.dream.admin.utils.DatabaseHelper;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.admin.domains.Host;
import build.dream.common.api.ApiRest;
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
import java.math.BigInteger;
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
        BigInteger id = saveModel.getId();
        BigInteger userId = saveModel.getUserId();
        String name = saveModel.getName();
        String ipAddress = saveModel.getIpAddress();
        int sshPort = saveModel.getSshPort();
        String userName = saveModel.getUserName();
        String password = saveModel.getPassword();
        int diskSize = saveModel.getDiskSize();
        int cupCoreQuantity = saveModel.getCupCoreQuantity();
        int memorySize = saveModel.getMemorySize();

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
            host.setDiskSize(diskSize);
            host.setCupCoreQuantity(cupCoreQuantity);
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
            host.setCreateUserId(userId);
            host.setLastUpdateUserId(userId);
            host.setDiskSize(diskSize);
            host.setCupCoreQuantity(cupCoreQuantity);
            host.setMemorySize(memorySize);
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
        installCommand.append(" --vcpus=" + cupCoreQuantity);
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
        int type = listModel.getType();
        int page = listModel.getPage();
        int rows = listModel.getRows();
        BigInteger hostId = listModel.getHostId();

        List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
        searchConditions.add(new SearchCondition("type", Constants.SQL_OPERATION_SYMBOL_EQUALS, type));
        if (type == 2) {
            searchConditions.add(new SearchCondition("parent_id", Constants.SQL_OPERATION_SYMBOL_EQUALS, hostId));
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

        return new ApiRest(data, "查询主机列表成功！");
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

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("开机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
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

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("关机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
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

        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("关机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
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

        JSchUtils.disconnectSession(session);

        childHost.setDeleted(true);
        DatabaseHelper.update(childHost);

        ApiRest apiRest = new ApiRest();
        apiRest.setMessage("删除虚拟机成功！");
        apiRest.setSuccessful(true);
        return apiRest;
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
        BigInteger id = updateModel.getId();
        int diskSize = updateModel.getDiskSize();
        int cupCoreQuantity = updateModel.getCupCoreQuantity();
        int memorySize = updateModel.getMemorySize();
        BigInteger userId = updateModel.getUserId();

        SearchModel childHostSearchModel = new SearchModel(true);
        childHostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, id);
        Host childHost = DatabaseHelper.find(Host.class, childHostSearchModel);
        Validate.notNull(childHost, "主机不存在！");

        SearchModel hostSearchModel = new SearchModel(true);
        hostSearchModel.addSearchCondition("id", Constants.SQL_OPERATION_SYMBOL_EQUALS, childHost.getParentId());
        Host host = DatabaseHelper.find(Host.class, hostSearchModel);
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
        vcpuElement.setText(String.valueOf(cupCoreQuantity));

        OutputFormat outputFormat = OutputFormat.createPrettyPrint();
        outputFormat.setEncoding(Constants.CHARSET_NAME_UTF_8);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLWriter xmlWriter = new XMLWriter(byteArrayOutputStream, outputFormat);

        xmlWriter.write(document);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        channelSftp.put(byteArrayInputStream, configFilePath, ChannelSftp.OVERWRITE);

        JSchUtils.disconnectChannel(channelSftp);
        JSchUtils.disconnectSession(session);

        ApiRest apiRest = new ApiRest();
        apiRest.setData(childHost);
        apiRest.setMessage("配置修改成功！");
        apiRest.setSuccessful(true);
        return apiRest;
    }
}
