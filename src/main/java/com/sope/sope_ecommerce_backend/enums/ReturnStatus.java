package com.sope.sope_ecommerce_backend.enums;

public enum ReturnStatus {
      REQUESTED, // Khách tạo yêu cầu
      APPROVED, // Shop/sàn duyệt cho trả
      REJECTED, // Shop/sàn từ chối
      PICKUP_SCHEDULED, // Đang sắp xếp shipper lấy hàng
      PICKED_UP, // Đã lấy hàng từ khách
      DELIVERED_TO_SHOP, // Đã trả về shop
      REFUND_INITIATED, // Shop khởi tạo hoàn tiền
      REFUNDED, // Đã hoàn tiền

      PENDING, // Trạng thái mặc định, khi tạo yêu cầu trả hàng
        CANCELLED, // Khách hủy yêu cầu trả hàng
        COMPLETED, // Yêu cầu trả hàng đã hoàn tất,
      NONE,
}