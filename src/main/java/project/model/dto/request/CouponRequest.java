package project.model.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CouponRequest {
    private String name;
    private float couponValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int userId;
    private int quantity;
}
