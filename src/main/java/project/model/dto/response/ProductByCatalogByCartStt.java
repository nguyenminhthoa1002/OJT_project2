package project.model.dto.response;

import lombok.Data;

@Data
public class ProductByCatalogByCartStt extends RootResponse {
    private int quantitySales;
    private float revenue;
    private float discount;
    private float realRevenue;
    private  String catalogName;


    public ProductByCatalogByCartStt(int id, String name, float realRevenue, int quantity, float discount, String catalogName,float revenue) {
        this.id=id;
        this.name=name;
        this.quantitySales=quantity;
        this.realRevenue=realRevenue;
        this.discount=discount;
        this.revenue= revenue ;
        this.catalogName=catalogName;
    }
}
