package project.model.dto.response;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
public class Revenue {
    private String date;
    private float totalTax;
    private float totalShip;
    private float totalDiscount;
    private float total;
    private int numberOder;

    public Revenue(String date, float totalTax, float totalShip, float totalDiscount, float total,int numberOder) {
        this.date = date;
        this.totalTax = totalTax;
        this.totalShip = totalShip;
        this.totalDiscount = totalDiscount;
        this.total = total;
        this.numberOder=numberOder;
    }

    public Revenue() {
    }
}
