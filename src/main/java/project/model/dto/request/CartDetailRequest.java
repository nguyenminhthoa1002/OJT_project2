package project.model.dto.request;

import lombok.Data;

@Data
public class CartDetailRequest {
    private int productId;
    private int quantity;
    private float price;
    private String status;
}
