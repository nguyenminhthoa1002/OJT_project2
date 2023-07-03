package project.model.dto.response;

import lombok.Data;

@Data
public class CouponResponse extends RootResponse{
    private float couponValue;
    private String expiration;
}
