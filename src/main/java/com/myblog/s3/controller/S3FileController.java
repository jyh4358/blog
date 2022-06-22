package com.myblog.s3.controller;

import com.myblog.s3.dto.S3FileResponse;
import com.myblog.s3.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3FileController {
    private final S3UploadService s3UploadService;

    @PostMapping("/file/upload")
    public String uploadFile(MultipartFile file) {
        System.out.println("file ====== " + file);
        String filePath = s3UploadService.uploadForMultiFile(file);
        S3FileResponse s3FileResponse = new S3FileResponse(file.getOriginalFilename(), filePath);
        System.out.println("s3FileResponse.getFilePath() = " + s3FileResponse.getFilePath());
        System.out.println("s3FileResponse.getFilePath() = " + s3FileResponse.getFileName());
        return s3FileResponse.getFilePath();
    }
}
