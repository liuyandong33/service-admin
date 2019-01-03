package build.dream.admin.controllers;

import build.dream.admin.models.file.ListFilesModel;
import build.dream.admin.services.FileService;
import build.dream.common.annotations.ApiRestAction;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.LogUtils;
import build.dream.common.utils.MimeMappingUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping(value = "/file")
public class FileController {
    @RequestMapping(value = "/listFiles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(modelClass = ListFilesModel.class, serviceClass = FileService.class, serviceMethodName = "listFiles", error = "操作失败")
    public String listFiles() {
        return null;
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        LogUtils.info(ApplicationHandler.getRequestHeaders().toString());
        File file = new File("C:\\Users\\liuyandong\\Desktop\\CentOS-7-x86_64-Minimal-1804.iso");
        long fileLength = file.length();

        String range = httpServletRequest.getHeader("Range");

        String fileName = file.getName();
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByFileName(fileName));
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        httpServletResponse.setHeader("Accept-Ranges", "bytes");
        httpServletResponse.setHeader("Content-Length", String.valueOf(fileLength));

        long start = 0;
        long end = 0;
        if (StringUtils.isNotBlank(range)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            range = range.substring(6);
            if (range.indexOf("-") == range.length() - 1) {
                start = Long.parseLong(range);
                end = fileLength - 1;
            } else if (range.indexOf("-") == 0) {
                long length = Long.parseLong(range.substring(1));
                start = fileLength - length;
                end = fileLength - 1;
            } else {
                String[] array = range.split("-");
                start = Long.parseLong(array[0]);
                end = Long.parseLong(array[1]);
            }

            httpServletResponse.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
        } else {
            start = 0;
            end = fileLength - 1;
        }

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = httpServletResponse.getOutputStream();
            inputStream.skip(start);
            long count = (end - start + 1) / 1024;
            int remainder = (int) ((end - start + 1) % 1024);
            byte[] buffer = new byte[1024];
            int length = 0;
            for (int index = 0; index < count; index++) {
                length = inputStream.read(buffer, 0, 1024);
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }

            if (remainder > 0) {
                inputStream.read(buffer, 0, remainder);
                outputStream.write(buffer, 0, remainder);
                outputStream.flush();
            }
        } catch (Exception e) {

        } finally {
            outputStream.close();
            inputStream.close();
        }
    }
}
