package com.myblog.s3.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.myblog.common.checker.RightLoginChecker;
import com.myblog.s3.properties.AmazonS3BucketProperties;
import com.myblog.s3.properties.AmazonS3CredentialsProperties;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.myblog.common.exception.ExceptionMessage.FAIL_IMAGE_UPLOAD;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3UploadService {
    private final AmazonS3CredentialsProperties amazonS3CredentialsProperties;
    private final AmazonS3BucketProperties amazonS3BucketProperties;
    private AmazonS3 amazonS3;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(amazonS3CredentialsProperties.getAccessKey(), amazonS3CredentialsProperties.getSecretKey());
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    public List<String> uploads(List<MultipartFile> multipartFiles) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String originalFilename = multipartFile.getOriginalFilename();

            putS3(multipartFile, originalFilename);
            fileUrls.add(getThumbnailPath(originalFilename));
        }

        return fileUrls;
    }

    // No content length specified for stream data.  Stream contents will be buffered in memory and could result in out of memory errors.
    // WARN 발생, 아래와 같이 코드 변경

    public String uploadForMultiFile(MultipartFile multipartFile, CustomOauth2User customOauth2User) {
        RightLoginChecker.checkAdminMember(customOauth2User);
        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            ObjectMetadata objMeta = new ObjectMetadata();
            byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());

            objMeta.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            amazonS3.putObject(new PutObjectRequest(
                    amazonS3BucketProperties.getBucket(),
                    multipartFile.getOriginalFilename(),
                    byteArrayInputStream,
                    objMeta
            ).withCannedAcl(CannedAccessControlList.PublicRead));
            return getThumbnailPath(originalFilename);
        } catch (Exception e) {
            throw FAIL_IMAGE_UPLOAD.getException();
        }

    }

    public String getThumbnailPath(String fineName) {
        return String.valueOf(amazonS3.getUrl(amazonS3BucketProperties.getBucket(), fineName));
    }

    private void putS3(MultipartFile multipartFile, String originalFilename) throws IOException {
        amazonS3.putObject(new PutObjectRequest(amazonS3BucketProperties.getBucket(), originalFilename, multipartFile.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
