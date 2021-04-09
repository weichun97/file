package com.fgoc.file.commons.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.fgoc.file.commons.exception.RRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author chun
 * @Date 2019/10/30 13:07
 */
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String uploadPath;
    private String prefixUrl;
    private String allowSuffix;
    private String maxFileSize;


    public String uploadMultipartFile(MultipartFile file, String dir){
        // 判断文件类型
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffix=fileName.substring(fileName.lastIndexOf(".") + 1);

        if(!Arrays.asList(allowSuffix.split(",")).contains(suffix)){
            throw new RRException("文件类型不允许");
        }

        // 判断文件大小
        long size = file.getSize();
        if (size /1024 / 1024 > Integer.valueOf(maxFileSize)) {
            throw new RRException("请选择小于"+ maxFileSize +"M的图片");
        }
        String today = DateUtil.today();
        if(!FileUtil.isDirectory(uploadPath + dir + "/" + today)){
            FileUtil.mkdir(uploadPath + dir + "/" + today);
        }

        String newName = IdUtil.simpleUUID();
        File newFile = new File(uploadPath + dir + "/" + today + "/" + newName + "." + suffix);
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RRException("上传文件失败，请稍后再试");
        }
        return prefixUrl + dir + "/" + today + "/" + newName + "." + suffix;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getPrefixUrl() {
        return prefixUrl;
    }

    public void setPrefixUrl(String prefixUrl) {
        this.prefixUrl = prefixUrl;
    }

    public String getAllowSuffix() {
        return allowSuffix;
    }

    public void setAllowSuffix(String allowSuffix) {
        this.allowSuffix = allowSuffix;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
