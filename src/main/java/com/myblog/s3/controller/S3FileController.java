package com.myblog.s3.controller;

import com.myblog.s3.dto.S3FileResponse;
import com.myblog.s3.service.S3UploadService;
import com.myblog.security.oauth2.model.CustomOauth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3FileController {
    private final S3UploadService s3UploadService;

    @PostMapping("/file/upload")
    public String uploadFile(
            MultipartFile file,
            @AuthenticationPrincipal CustomOauth2User customOauth2User
            ) {

        String filePath = s3UploadService.uploadForMultiFile(file, customOauth2User);
        S3FileResponse s3FileResponse = new S3FileResponse(file.getOriginalFilename(), filePath);
        return s3FileResponse.getFilePath();
    }
}
