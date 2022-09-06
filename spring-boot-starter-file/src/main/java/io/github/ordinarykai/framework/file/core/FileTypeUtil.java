package io.github.ordinarykai.framework.file.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author wukai
 * @date 2022/8/24 16:09
 */
public class FileTypeUtil {

    /**
     * 判断文件是否允许上传并返回文件类型
     *
     * @param allowedTypes     允许上传的文件类型
     * @param bytes            上传文件字节数据
     * @param originalFilename 上传文件原始文件名
     */
    public static String allowUpload(List<FileTypeEnum> allowedTypes, byte[] bytes, String originalFilename) throws IOException {
        // 随机文件名
        String type = cn.hutool.core.io.FileTypeUtil.getType(new ByteArrayInputStream(bytes), originalFilename);
        if (StrUtil.isBlank(type)) {
            throw new RuntimeException("Unknown file type");
        }
        if (CollectionUtil.isNotEmpty(allowedTypes) && !allowedTypes.contains(FileTypeEnum.getByType(type))) {
            throw new RuntimeException("The file type is not be allowed. Allowed file types are " + allowedTypes);
        }
        return type;
    }

}
