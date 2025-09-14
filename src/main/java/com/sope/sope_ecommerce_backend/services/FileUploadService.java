package com.sope.sope_ecommerce_backend.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadImage(MultipartFile file);
    String uploadVideo(MultipartFile file);
}
