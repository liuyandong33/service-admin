package build.dream.admin.utils;

import build.dream.admin.constants.Constants;
import build.dream.common.admin.domains.Host;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.Validate;

import java.io.IOException;
import java.math.BigInteger;

public class VirtualMachineUtils {
    public static String operate(BigInteger hostId, BigInteger userId, String operateType) throws JSchException, IOException {
        Host childHost = DatabaseHelper.find(Host.class, hostId);
        Validate.notNull(childHost, "主机不存在！");

        Host host = DatabaseHelper.find(Host.class, childHost.getParentId());
        Validate.notNull(host, "宿主机不存在！");

        Session session = JSchUtils.createSession(host.getUserName(), host.getPassword(), host.getIpAddress(), host.getSshPort());

        String name = childHost.getName();
        String command = null;
        if (Constants.OPERATE_TYPE_START.equals(operateType)) {
            command = "virsh start " + name;
        } else if (Constants.OPERATE_TYPE_SHUTDOWN.equals(operateType)) {
            command = "virsh shutdown " + name;
        } else if (Constants.OPERATE_TYPE_DESTROY.equals(operateType)) {
            command = "virsh destroy " + name;
        } else if (Constants.OPERATE_TYPE_REBOOT.equals(operateType)) {
            command = "virsh reboot " + name;
        } else if (Constants.OPERATE_TYPE_SUSPEND.equals(operateType)) {
            command = "virsh suspend " + name;
        } else if (Constants.OPERATE_TYPE_RESUME.equals(operateType)) {
            command = "virsh resume " + name;
        } else if (Constants.OPERATE_TYPE_UNDEFINE.equals(operateType)) {
            command = "virsh undefine " + name;
            childHost.setDeleted(true);
            DatabaseHelper.update(childHost);
        }
        String result = JSchUtils.executeCommand(session, command);

        JSchUtils.disconnectSession(session);
        return result;
    }
}
