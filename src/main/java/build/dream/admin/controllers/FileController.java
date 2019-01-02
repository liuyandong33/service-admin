package build.dream.admin.controllers;

import build.dream.admin.models.file.ListFilesModel;
import build.dream.admin.services.FileService;
import build.dream.common.annotations.ApiRestAction;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.IOUtils;
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
        File file = new File("F:\\迅雷下载\\cn_windows_server_2019_x64_dvd_4de40f33.iso");
        long fileLength = file.length();

        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = httpServletResponse.getOutputStream();
        String range = httpServletRequest.getHeader("Range");

        String fileName = file.getName();
        httpServletResponse.setContentType(MimeMappingUtils.obtainMimeTypeByFileName(fileName));
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        httpServletResponse.setHeader("Accept-Ranges", "bytes");
        httpServletResponse.setHeader("Content-Length", String.valueOf(fileLength));
        if (StringUtils.isNotBlank(range)) {
            range = range.substring(6);
            long start = 0;
            long end = 0;
            if (range.indexOf("-") == range.length() - 1) {
                start = Long.parseLong(range);
                end = fileLength - 1;
            } else if (range.indexOf("-") == 0) {
                long length = Long.parseLong(range.substring(1));
                start = fileLength - end;
                end = fileLength - 1;
            } else {
                String[] array = range.split("-");
                start = Long.parseLong(array[0]);
                end = Long.parseLong(array[1]);
            }

            httpServletResponse.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            System.out.println("bytes " + start + "-" + end + "/" + fileLength);
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            randomAccessFile.seek(start);
//            inputStream.skip(start);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            while (randomAccessFile.getFilePointer() <= end) {
                bufferedOutputStream.write(randomAccessFile.read());
            }
            randomAccessFile.close();
            bufferedOutputStream.close();
        } else {
            IOUtils.copy(inputStream, outputStream);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
