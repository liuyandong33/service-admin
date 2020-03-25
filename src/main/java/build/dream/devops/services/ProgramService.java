package build.dream.devops.services;

import build.dream.common.api.ApiRest;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.devops.constants.ConfigurationKeys;
import build.dream.devops.constants.Constants;
import build.dream.devops.models.program.ListProgramsModel;
import build.dream.devops.models.program.ListVersionsModel;
import build.dream.devops.utils.JSchUtils;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
public class ProgramService {
    private static final String REPOSITORY_HOST_IP_ADDRESS = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_IP_ADDRESS);
    private static final String REPOSITORY_HOST_USERNAME = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_USERNAME);
    private static final String REPOSITORY_HOST_PASSWORD = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_PASSWORD);
    private static final int REPOSITORY_HOST_SSH_PORT = Integer.parseInt(ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_HOST_SSH_PORT));
    private static final String REPOSITORY_PATH = ConfigurationUtils.getConfiguration(ConfigurationKeys.REPOSITORY_PATH);

    public ApiRest listPrograms(ListProgramsModel listProgramsModel) {
        List<String> programs = new ArrayList<String>();
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = JSchUtils.createSession(REPOSITORY_HOST_USERNAME, REPOSITORY_HOST_PASSWORD, REPOSITORY_HOST_IP_ADDRESS, REPOSITORY_HOST_SSH_PORT);
            channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
            channelSftp.connect();
            Vector vector = channelSftp.ls(REPOSITORY_PATH);

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
        String name = listVersionsModel.getName();

        List<String> versions = new ArrayList<String>();
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = JSchUtils.createSession(REPOSITORY_HOST_USERNAME, REPOSITORY_HOST_PASSWORD, REPOSITORY_HOST_IP_ADDRESS, REPOSITORY_HOST_SSH_PORT);
            channelSftp = (ChannelSftp) JSchUtils.openChannel(session, Constants.CHANNEL_TYPE_SFTP);
            channelSftp.connect();
            Vector vector = channelSftp.ls(REPOSITORY_PATH + "/" + name);

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
