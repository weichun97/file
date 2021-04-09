package com.fgoc.file.commons.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.fgoc.file.commons.exception.RRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The type Upload util.
 *
 * @Author chun
 * @Date 2019 /10/30 13:07
 */
@Component
@ConfigurationProperties(prefix = "upload")
public class UploadUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String appId;
    private String appSecret;
    private String uploadPath;
    private String prefixUrl;
    private String allowSuffix;
    private String maxFileSize;
    private int maxMinute;

    /**
     * Upload multipart file string.
     *
     * @param file the file
     * @param dir  the dir
     * @return the string
     */
    public String uploadMultipartFile(MultipartFile file, String dir){
        // 判断文件类型
        // 获取文件名
        if(file == null){
            throw new RRException("请上传文件");
        }

        String fileName = file.getOriginalFilename();
        // 获取文件后缀
        String suffix=fileName.substring(fileName.lastIndexOf(".") + 1);

        if(!Arrays.asList(allowSuffix.split(",")).contains(suffix)){
            throw new RRException("文件类型不允许");
        }

        // 判断文件大小
        long size = file.getSize();
        if (size /1024 / 1024 > Integer.parseInt(maxFileSize)) {
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

    /**
     * 上传文件
     *
     * @param file      文件
     * @param path      保存文件夹
     * @param nonce     随机字符串
     * @param timestamp 上传时间戳
     * @param appId     应用id
     * @param sign      签名
     * @return the string
     */
    public String uploadMultipartFile(MultipartFile file, String path, String nonce, String timestamp, String appId, String sign) {
        checkAppId(appId);
        checkTimestamp(timestamp);
        if(!checkSign(sign, path, nonce, timestamp, appId)){
            throw new RRException("签名验证失败");
        }
        return uploadMultipartFile(file, path);
    }

    /**
     * 检测 appId
     * @param inputAppId
     */
    private void checkAppId(String inputAppId) {
        if(!appId.equals(inputAppId)){
            throw new RRException("appId不正确");
        }
    }

    /**
     * 校验时间戳
     * @param timestamp 时间戳
     */
    private void checkTimestamp(String timestamp) {
        Date date = null;
        try {
            date = new Date(Long.parseLong(timestamp + "000"));
        } catch (NumberFormatException e) {
            throw new RRException("时间戳格式不正确");
        }
        if(DateUtil.between(date, new Date(), DateUnit.MINUTE) > maxMinute){
            throw new RRException("时间戳与当前时间间隔过长");
        }
    }

    /**
     * 校验签名
     * @param sign 签名
     * @param path 文件地址
     * @param nonce 随机字符串
     * @param timestamp 时间戳
     * @param appId 应用id
     * @return
     */
    private boolean checkSign(String sign, String path, String nonce, String timestamp, String appId) {
        List<String> paramList = CollUtil.newArrayList(path, nonce, timestamp, appId, appSecret);
        paramList.sort(String::compareTo);
        return sign != null && sign.equals(SecureUtil.sha1(StrUtil.join("", paramList.toArray())));
    }

    /**
     * Gets upload path.
     *
     * @return the upload path
     */
    public String getUploadPath() {
        return uploadPath;
    }

    /**
     * Sets upload path.
     *
     * @param uploadPath the upload path
     */
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    /**
     * Gets prefix url.
     *
     * @return the prefix url
     */
    public String getPrefixUrl() {
        return prefixUrl;
    }

    /**
     * Sets prefix url.
     *
     * @param prefixUrl the prefix url
     */
    public void setPrefixUrl(String prefixUrl) {
        this.prefixUrl = prefixUrl;
    }

    /**
     * Gets allow suffix.
     *
     * @return the allow suffix
     */
    public String getAllowSuffix() {
        return allowSuffix;
    }

    /**
     * Sets allow suffix.
     *
     * @param allowSuffix the allow suffix
     */
    public void setAllowSuffix(String allowSuffix) {
        this.allowSuffix = allowSuffix;
    }

    /**
     * Gets max file size.
     *
     * @return the max file size
     */
    public String getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Sets max file size.
     *
     * @param maxFileSize the max file size
     */
    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public int getMaxMinute() {
        return maxMinute;
    }

    public void setMaxMinute(int maxMinute) {
        this.maxMinute = maxMinute;
    }
}
