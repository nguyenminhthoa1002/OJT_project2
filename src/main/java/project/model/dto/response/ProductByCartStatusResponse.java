package project.model.dto.response;

import lombok.Data;

@Data
public class ProductByCartStatusResponse extends RootResponse{
    private int quantity;
    private float exportPrice;
    private int disCount;

    public ProductByCartStatusResponse(int id, String name, float exportPrice, int quantity, int discount) {
        this.id=id;
        this.name=name;
        this.exportPrice=exportPrice;
        this.quantity=quantity;
        this.disCount=discount;
    }
}
