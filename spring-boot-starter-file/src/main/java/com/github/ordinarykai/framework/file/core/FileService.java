package com.github.ordinarykai.framework.file.core;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author wukai
 * @date 2022/8/11 17:19
 */
public interface FileService {

    /**
     * @param file 上传文件
     * @return 文件url
     */
    String upload(MultipartFile file);

}
