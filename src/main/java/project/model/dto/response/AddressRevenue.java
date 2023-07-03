package project.model.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AddressRevenue {
    private String date;
    private float totalTax;
    private float totalShip;
    private float totalDiscount;
    private float total;
    private int numberOder;
   private String city;
    public AddressRevenue(String date, float totalTax, float totalShip, float totalDiscount, float total,int numberOder,String city) {
        this.date=date;
        this.totalTax=totalTax;
        this.totalShip=(totalShip);
        this.totalDiscount=(totalDiscount);
        this.total=total;
        this.numberOder=numberOder;
        this.city=city;
    }
    public AddressRevenue() {
    }
}
