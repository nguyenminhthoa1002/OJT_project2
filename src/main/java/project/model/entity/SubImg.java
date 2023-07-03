package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "subImg")
public class SubImg extends BaseEntity{
    @Column(columnDefinition = "text")
    private String subLink;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn( name = "productId")
    @JsonIgnore
    private Product product;

}
