package project.model.dto.response;

import lombok.Data;

@Data
public class ProductByCartSttCancel extends RootResponse{
    private int quantity;
    private float exportPrice;
    private int disCount;
    private String catalogName;


    public ProductByCartSttCancel(int id, String name, float exportPrice, int quantity, int discount,String catalogName) {
        this.id=id;
        this.name=name;
        this.exportPrice=exportPrice;
        this.quantity=quantity;
        this.disCount=discount;
        this.catalogName=catalogName;
    }
}
