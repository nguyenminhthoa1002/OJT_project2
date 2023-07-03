package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name="brand")
public class Brand extends BaseEntity {
    @Column(columnDefinition = "text")
    private String brandLogo;
    @OneToMany(mappedBy = "brand")
    @JsonIgnore
    private List<Product> productList= new ArrayList<>();
}
