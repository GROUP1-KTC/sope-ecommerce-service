package com.sope.sope_ecommerce_backend.dto.request;

import com.sope.sope_ecommerce_backend.enums.ProgramStatus;

public record ChangeStatusRequest(ProgramStatus status) {
}
