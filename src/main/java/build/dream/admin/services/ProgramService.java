package build.dream.admin.services;

import build.dream.admin.constants.Constants;
import build.dream.admin.models.program.ListProgramsModel;
import build.dream.admin.models.program.ListVersionsModel;
import build.dream.admin.utils.JSchUtils;
import build.dream.common.api.ApiRest;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
public class ProgramService {
    public ApiRest listPrograms(ListProgramsModel listProgramsModel) {
        String type = listProgramsModel.getType();

        List<String> programs = new ArrayList<String>();
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = JSchUtils.createSession("root", "root", "192.168.1.10", 22);
            channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
            channelSftp.connect();
            String path = "/usr/local/development/";
            if ("snapshot".equals(type)) {
                path += "snapshots";
            } else if ("release".equals(type)) {
                path += "releases";
            }
            Vector vector = channelSftp.ls(path);

            for (int index = 0; index < vector.size(); index++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) vector.get(index);
                String fileName = lsEntry.getFilename();
                if (".".equals(fileName) || "..".equals(fileName)) {
                    continue;
                }
                programs.add(fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JSchUtils.disconnectChannel(channelSftp);
            JSchUtils.disconnectSession(session);
        }
        return ApiRest.builder().data(programs).message("查询程序列表成功！").successful(true).build();
    }

    public ApiRest listVersions(ListVersionsModel listVersionsModel) {
        String type = listVersionsModel.getType();
        String name = listVersionsModel.getName();

        List<String> versions = new ArrayList<String>();
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = JSchUtils.createSession("root", "root", "192.168.1.10", 22);
            channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
            channelSftp.connect();
            String path = "/usr/local/development/";
            if ("snapshot".equals(type)) {
                path += "snapshots/" + name;
            } else if ("release".equals(type)) {
                path += "releases/" + name;
            }
            Vector vector = channelSftp.ls(path);

            for (int index = 0; index < vector.size(); index++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) vector.get(index);
                String fileName = lsEntry.getFilename();
                if (".".equals(fileName) || "..".equals(fileName)) {
                    continue;
                }
                versions.add(fileName.substring(name.length() + 1, fileName.length() - 4));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JSchUtils.disconnectChannel(channelSftp);
            JSchUtils.disconnectSession(session);
        }
        return ApiRest.builder().data(versions).message("查询程序列表成功！").successful(true).build();
    }
}
