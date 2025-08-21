package com.sope.sope_ecommerce_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CreateVNPayRequest(
        @JsonProperty("vnp_Version")   String vnpVersion,
        @JsonProperty("vnp_Command")   String vnpCommand,
        @JsonProperty("vnp_TmnCode")   String vnpTmnCode,
        @JsonProperty("vnp_Amount")    String vnpAmount,
        @JsonProperty("vnp_BankCode")  String vnpBankCode,
        @JsonProperty("vnp_CreateDate")String vnpCreateDate,
        @JsonProperty("vnp_CurrCode")  String vnpCurrCode,
        @JsonProperty("vnp_IpAddr")    String vnpIpAddr,
        @JsonProperty("vnp_Locale")    String vnpLocale,
        @JsonProperty("vnp_OrderInfo") String vnpOrderInfo,
        @JsonProperty("vnp_OrderType") String vnpOrderType,
        @JsonProperty("vnp_ReturnUrl") String vnpReturnUrl,
        @JsonProperty("vnp_ExpireDate")String vnpExpireDate,
        @JsonProperty("vnp_TxnRef")    String vnpTxnRef,
        @JsonProperty("vnp_SecureHash")String vnpSecureHash
) {
}
