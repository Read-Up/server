package com.readup.server.common.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class R2UploaderTest {

    private S3Client s3Client;
    private R2Uploader r2Uploader;

    @BeforeEach
    void setUp() {
        s3Client = mock(S3Client.class);
        r2Uploader = new R2Uploader(s3Client);

        TestUtil.setField(r2Uploader, "bucket", "test-bucket");
        TestUtil.setField(r2Uploader, "cdnDomain", "https://cdn.test.com/");
    }

    @Test
    void 이미지_단건업로드() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "data".getBytes()
        );

        // when
        String result = r2Uploader.uploadSingle(file, 1L, "profile");

        // then
        ArgumentCaptor<PutObjectRequest> captor = ArgumentCaptor.forClass(PutObjectRequest.class);
        verify(s3Client, times(1)).putObject(captor.capture(), any(RequestBody.class));

        PutObjectRequest request = captor.getValue();
        assertThat(request.bucket()).isEqualTo("test-bucket");
        assertThat(request.aclAsString()).isEqualTo("public-read");
        assertThat(result).startsWith("https://cdn.test.com/1_profile_");
    }

    @Test
    void 이미지_삭제() {
        // given
        String key = "1_profile_xyz.jpg";

        // when
        r2Uploader.delete(key);

        // then
        ArgumentCaptor<DeleteObjectRequest> captor = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client).deleteObject(captor.capture());
        assertThat(captor.getValue().key()).isEqualTo(key);
        assertThat(captor.getValue().bucket()).isEqualTo("test-bucket");
    }
}
