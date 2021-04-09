package com.fgoc.file.controller;

import com.fgoc.file.commons.utils.R;
import com.fgoc.file.commons.utils.UploadUtil;
import com.fgoc.file.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author chun
 * @Date 2019/10/30 11:48
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UploadService uploadService;

    @PostMapping("")
    public R upload(MultipartFile file, String path, String nonce, String timestamp, String appId, String sign) {
        return R.ok().data(uploadService.upload(file, path, nonce, timestamp, appId, sign));
    }
}
