package project.model.dto.response;


import lombok.Data;

@Data
public class ProductReportByLocation extends RootResponse{
    private int quantitySales;
    private float revenue;
    private float discount;
    private float realRevenue;
    private  String locationName;
    public ProductReportByLocation(int id,String name,int quantitySales,float revenue,float discount,float realRevenue,String locationName){
        this.id=id;
        this.name=name;
        this.quantitySales=quantitySales;
        this.revenue=revenue;
        this.discount=discount;
        this.realRevenue=realRevenue;
        this.locationName=locationName;
    }
}
