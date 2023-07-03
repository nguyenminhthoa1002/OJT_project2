package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product")
public class Product extends BaseEntity{
    @Column(columnDefinition = "text")
    private String productDescriptions;
    private String title;
    private int discount;
    private int productQuantity;
    @Column(columnDefinition = "text")
    private String productImg;
    private float importPrice;
    private float exportPrice;
    private LocalDateTime creatDate;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn(name = "catalogId")
    private Catalog catalog;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brandId")
    private Brand brand;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private Location location;
    @OneToMany (mappedBy = "product")
    @JsonIgnore
    private List<Review> reviewList = new ArrayList<>();
    @OneToMany (mappedBy = "product")
    private List<SubImg> subImgList= new ArrayList<>();
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Wish> wishList= new ArrayList<>();
    @OneToMany (mappedBy = "product")
    @JsonIgnore
    private List<CartDetail > cartDetailList = new ArrayList<>();
    @OneToMany (mappedBy = "product")
    @JsonIgnore
    private List<FlashSale> saleList= new ArrayList<>();
}
