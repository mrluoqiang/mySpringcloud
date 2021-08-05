package com.study.aliyun.config;

import com.study.aliyun.utils.AliyunOssUtil;
import com.study.common.constant.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class test {

    @RequestMapping("/uploadFileToOss")
    @ApiOperation(value = "上传文件",httpMethod = "POST")
    public Result uploadFileToOss(MultipartFile file) throws IOException {
        if(ObjectUtils.isEmpty(file)||file.getSize()<=0){
            return Result.fail("文件不能为空");
        }
        File uploadFile = AliyunOssUtil.multipartFileToFile(file);
        String s = AliyunOssUtil.uploadFile(uploadFile);
        AliyunOssUtil.delteTempFile(uploadFile);
        return  Result.ok(s);
    }

    @RequestMapping("/downloadFileToOss")
    @ApiOperation(value = "下载文件")
    public Result downloadTestImg(String fileId, HttpServletResponse response){
        if(StringUtils.isEmpty(fileId)){
            return Result.fail("文件为空");
        }
        AliyunOssUtil.downloadFile(fileId,response);
        return  Result.ok("文件下载成功");
    }
}
