package project.model.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class FlashSaleRequest {
    private int productId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int discount;
    private int quantity;
    private int status;
    private int sold;
}
