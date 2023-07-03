package project.model.dto.response;

import lombok.Data;

@Data
public class CartDetailResponse extends RootResponse{
    private int quantity;
    private float price;
    private int productId;
}
