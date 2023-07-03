package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cartDetail")
public class CartDetail extends BaseEntity{
    private int quantity;
    private float price;
    private float discount;
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "cartId")
    @JsonIgnore
    private Cart cart;
}
