package project.model.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlashSaleResponse extends RootResponse{
    private int discount;
    private int quantity;
    private String img;
    private float price;
    private int sold;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
