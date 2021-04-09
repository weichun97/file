package com.fgoc.file.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Upload service.
 */
public interface UploadService {

    /**
     * 上传文件
     *
     * @param file      the file
     * @param path      the path
     * @param nonce     the nonce
     * @param timestamp the timestamp
     * @param appId     the app id
     * @param sign      the sign
     * @return the string
     */
    String upload(MultipartFile file, String path, String nonce, String timestamp, String appId, String sign);
}
