package project.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table (name = "cart")
public class Cart extends BaseEntity{
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private String state;
    @Column(columnDefinition = "text")
    private String note;
    private LocalDateTime creatDate;
    private float total;
    private float discount;
    private float shipping;
    private float tax;
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Users users;
    @OneToMany(mappedBy = "cart")
    private List<CartDetail> cartDetailList= new ArrayList<>();
}
