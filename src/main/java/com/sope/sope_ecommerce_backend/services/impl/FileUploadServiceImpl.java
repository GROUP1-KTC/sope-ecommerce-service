package com.sope.sope_ecommerce_backend.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sope.sope_ecommerce_backend.services.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        return upload(file, "image");
    }

    @Override
    public String uploadVideo(MultipartFile file) {
        return upload(file, "video");
    }

    private String upload(MultipartFile file, String type) {
        if (file != null && !file.isEmpty()) {
            try {
                Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                        "video".equals(type) ? ObjectUtils.asMap("resource_type", "video")
                                : ObjectUtils.emptyMap());
                return (String) uploadResult.get("secure_url");
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload " + type, e);
            }
        }
        return null;
    }
}
