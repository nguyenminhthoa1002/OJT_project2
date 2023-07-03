package project.model.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RevenueAddress {
    private LocalDate startDate;
    private LocalDate endDate;
    private float totalTax;
    private float totalShip;
    private float totalDiscount;
    private float total;
    private String city;
}
