package build.dream.admin.controllers;

import build.dream.admin.models.file.ListFilesModel;
import build.dream.admin.services.FileService;
import build.dream.common.annotations.ApiRestAction;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.FileUtils;
import build.dream.common.utils.LogUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

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
    public void download(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        LogUtils.info(ApplicationHandler.getRequestHeaders().toString());
        File file = new File("C:\\Users\\liuyandong\\Desktop\\CentOS-7-x86_64-Minimal-1804.iso");
        FileUtils.download(httpServletRequest, httpServletResponse, file, true);
    }
}
