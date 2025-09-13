package com.sope.sope_ecommerce_backend.dto.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ShopCreateForm {
    private String requestJson;
    private MultipartFile logoFile;
    private MultipartFile taxFile;
    private MultipartFile idFront;
    private MultipartFile idBack;
    private MultipartFile selfie;
}
