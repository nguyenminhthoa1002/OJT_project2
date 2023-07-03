package project.model.entity;
import lombok.Data;
import project.model.shopMess.Constants;

import javax.persistence.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "flashSale")
public class FlashSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "nvarchar(100)")
    private String name;
    private int status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int discount;
    private int quantity;
    private int sold;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;
    public FlashSale() {
    }
    public FlashSale(int id, String name, int status, LocalDateTime startTime, LocalDateTime endTime, int discount, int quantity, int sold, Product product) {
        this.id = id;
        this.product = product;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.discount = discount;
        this.quantity = quantity;
        this.sold = sold;

    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        if (this.getQuantity()!=this.getSold()&&((LocalDateTime.now().compareTo(this.getStartTime())>0)&&(LocalDateTime.now().compareTo(this.getEndTime())<0))){
            this.status= Constants.ONLINE;
        }else {
            this.status=Constants.OFFLINE;
        };
        return status;
    }
    public void setStatus(int status) {
        this.status=status;
    }
    public int getSold() {
        return sold;
    }
    public void setSold(int sold) {
        if (this.getQuantity()!=this.getSold()&&((LocalDateTime.now().compareTo(this.getStartTime())>0)&&(LocalDateTime.now().compareTo(this.getEndTime())<0))){
            this.setStatus(Constants.ONLINE);
            this.sold = sold;
        }else {
            this.setStatus(Constants.OFFLINE);
        }
    }
}
