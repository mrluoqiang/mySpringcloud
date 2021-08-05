package com.study.aliyun.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.study.aliyun.config.oss.AliYunOssParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;

public class AliyunOssUtil {

    //multipartFile文件转换File
    public static File multipartFileToFile(MultipartFile file) {
        File toFile = null;
        try {
            if (file.equals("") || file.getSize() <= 0) {
                file = null;
            } else {
                InputStream ins = null;
                ins = file.getInputStream();
                toFile = new File(file.getOriginalFilename());
                inputStreamToFile(ins, toFile);
                ins.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
    //获取OSSClient
    public static OSS getOSSClient(){
        OSS ossClient = new OSSClientBuilder().build(AliYunOssParam.ossEndpoint, AliYunOssParam.ossAccessKeyId, AliYunOssParam.ossAccessKeySecret);
        return ossClient;
    }
    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static String uploadFile(File file) {
        String fileSuffix = "";
        fileSuffix = file.getName().substring(file.getName().lastIndexOf("."), file.getName().length());
        // 创建OSSClient实例。
        OSS ossClient = getOSSClient();
        // 创建PutObjectRequest对象。
        // 依次填写Bucket名称（例如examplebucket）、Object完整路径（例如exampledir/exampleobject.txt）和本地文件的完整路径。Object完整路径中不能包含Bucket名称。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        String name = new java.text.SimpleDateFormat("yyyyMMddhhmmss").format(new Date());    //获取当前日期
        name = name + (int) (Math.random() * 90000 + 10000) + fileSuffix;
        PutObjectRequest putObjectRequest = new PutObjectRequest(AliYunOssParam.ossBucketName, name, file);
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);
        // 上传文件。
        ossClient.putObject(putObjectRequest);
        // 关闭OSSClient。
        ossClient.shutdown();
        return name;
    }

    /**
     * 下载文件
     */
    public static void downloadFile(String fileName, HttpServletResponse response) {
        // 创建OSSClient实例。
        OSS ossClient = getOSSClient();
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(AliYunOssParam.ossBucketName, fileName);
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(ossObject.getObjectContent());
        // 清空response
        response.reset();
        // 设置response的Header
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        //以下代码图片下载时 必须注掉 否则会导致图片失真
        /* response.addHeader("Content-Length", "" + fis.available()); */
        response.setContentType("application/octet-stream");
        try {
            int len = 0;
            byte[] buffer = new byte[1024];
            OutputStream out = response.getOutputStream();
            while ((len = fis.read(buffer)) > 0) {
                // 将缓冲区的数据输出到客户端浏览器
                out.write(buffer, 0, len);
            }
            fis.close();
            out.flush();
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
