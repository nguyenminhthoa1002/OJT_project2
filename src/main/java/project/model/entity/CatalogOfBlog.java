package project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Table(name = "catalogOfBlog")
@Entity
public class CatalogOfBlog extends BaseEntity {
    @OneToMany (mappedBy = "catalogOfBlog")
    @JsonIgnore
    private List<Blog> blogList= new ArrayList<>();
}
