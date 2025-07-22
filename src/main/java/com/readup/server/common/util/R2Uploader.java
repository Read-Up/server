package com.readup.server.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class R2Uploader {

    private final S3Client s3Client;

    @Value("${cloud.r2.bucket}")
    private String bucket;

    @Value("${cloud.r2.cdn-domain}")
    private String cdnDomain;

    public String uploadSingle(MultipartFile file, Long userId, String purpose) {
        String url = userId + "_" + purpose + "_" + UUID.randomUUID();
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(url)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();

            s3Client.putObject(putRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            rollbackOnTransaction(url);

            return cdnDomain + url;
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
    }

    private void rollbackOnTransaction(String url) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                        @Override
                        public void afterCompletion(int status) {
                            if (status == STATUS_ROLLED_BACK) {
                                log.warn("[R2Uploader] 트랜잭션 롤백 발생 → 업로드된 객체 삭제: {}", url);
                                delete(url);
                            }
                        }
                    });
        }
    }

    public void delete(String key) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3Client.deleteObject(request);
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage(), e);
        }
    }

    // 확장용 메서드 (복수 업로드)
    public List<String> uploadMultiple(List<MultipartFile> files, Long userId, String purpose) {
        String folder = userId + "_" + purpose;
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadToFolder(file, folder));
        }
        return urls;
    }

    private String uploadToFolder(MultipartFile file, String folder) {
        String filename = UUID.randomUUID().toString();
        String url = folder + "/" + filename;

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(url)
                    .contentType(file.getContentType())
                    .acl("public-read")
                    .build();

            s3Client.putObject(putRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            rollbackOnTransaction(url);

            return cdnDomain + url;
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage(), e);
        }
    }
}
