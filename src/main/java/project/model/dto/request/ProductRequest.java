package project.model.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductRequest {
    private String name;
    private String productDescriptions;
    private String title;
    private int discount;
    private int productQuantity;
    private String productImg;
    private float importPrice;
    private float exportPrice;
    private LocalDateTime creatDate;
    private int catalogId;
    private int brandId;
    private int status;
}
