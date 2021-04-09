package com.fgoc.file.service.impl;

import com.fgoc.file.commons.utils.UploadUtil;
import com.fgoc.file.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Upload service.
 */
@Service
public class UploadServiceImpl implements UploadService {

    /**
     * The Upload util.
     */
    @Autowired
    UploadUtil uploadUtil;

    @Override
    public String upload(MultipartFile file, String path, String nonce, String timestamp, String appId, String sign){
        return uploadUtil.uploadMultipartFile(file, path, nonce, timestamp, appId, sign);
    }
}
