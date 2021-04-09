package com.fgoc.file.controller;

import com.fgoc.file.commons.utils.R;
import com.fgoc.file.commons.utils.UploadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    UploadUtil uploadPath;

    @RequestMapping("")
    public R upload(MultipartFile file, String path) {
        return R.ok().data(uploadPath.uploadMultipartFile(file, path));
    }
}
