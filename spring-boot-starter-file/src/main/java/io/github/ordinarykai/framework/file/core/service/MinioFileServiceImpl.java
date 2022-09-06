package io.github.ordinarykai.framework.file.core.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import io.github.ordinarykai.framework.common.exception.ApiException;
import io.github.ordinarykai.framework.file.config.FileUploadProperties;
import io.github.ordinarykai.framework.file.core.FileTypeEnum;
import io.github.ordinarykai.framework.file.core.FileTypeUtil;
import io.minio.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author wukai
 * @date 2022/8/11 17:19
 */
@Data
@AllArgsConstructor
public class MinioFileServiceImpl implements FileService {

    private final FileUploadProperties.MinioFileUploadProperties properties;
    private final List<FileTypeEnum> allowedTypes;
    private final MinioClient minioClient;
    /**
     * 只读策略
     */
    private final String WRITE_ONLY_POLICY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\",\"s3:ListBucketMultipartUploads\"],\"Resource\":[\"arn:aws:s3:::%s\"]},{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}";

    public MinioFileServiceImpl(FileUploadProperties properties) {
        this.properties = properties.getMinio();
        this.allowedTypes = properties.getTypes();
        this.minioClient = MinioClient.builder()
                .credentials(this.properties.getAccessKey(), this.properties.getSecretKey())
                .endpoint(this.properties.getEndpoint(), this.properties.getPort(), this.properties.isSecure())
                .build();
    }

    @Override
    public String upload(MultipartFile file) {
        try {
            // 创建存储桶
            String bucketName = properties.getBucketName() + LocalDate.now().format(DateTimeFormatter.ofPattern("-yyyyMM"));
            this.makeBucket(bucketName);
            // 文件类型校验
            byte[] bytes = IoUtil.readBytes(file.getInputStream());
            String type = FileTypeUtil.allowUpload(allowedTypes, bytes, file.getOriginalFilename());
            // 随机对象名
            String objectName = UUID.fastUUID().toString(true) + "." + type;
            // 存储
            this.putObject(bucketName, bytes, objectName, file.getContentType());
            return properties.getEndpoint() + ":" + properties.getPort() + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            // TODO: 2022/9/6  
            throw new ApiException("上传失败");
        }
    }

    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return true:存在 false:不存在
     */
    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建存储桶并设置为PUBLIC
     *
     * @param bucketName 存储桶名称
     */
    public void makeBucket(String bucketName) throws Exception {
        if (bucketExists(bucketName)) {
            return;
        }
        //  创建存储桶
        MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                .bucket(bucketName)
                .build();
        minioClient.makeBucket(makeBucketArgs);
        // 设置为只读策略
        SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(String.format(WRITE_ONLY_POLICY, bucketName, bucketName))
                .build();
        minioClient.setBucketPolicy(setBucketPolicyArgs);
    }

    /**
     * 文件上传
     *
     * @param bucketName 存储桶名称
     * @param bytes      上传文件的字节数据
     */
    public void putObject(String bucketName, byte[] bytes, String filename, String fileType) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .stream(inputStream, -1, 5 * 1024 * 1024)
                .contentType(fileType)
                .build();
        minioClient.putObject(putObjectArgs);
    }

}
