package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "location")
public class Location extends BaseEntity{
    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private List<Product> productList= new ArrayList<>();
}
