package project.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "wish")
public class Wish extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;
    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Users users;
}
