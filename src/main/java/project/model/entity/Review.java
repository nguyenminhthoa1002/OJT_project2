package project.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Table(name = "review")
@Entity
public class Review extends BaseEntity{
    private int starPoint;
    @Column(columnDefinition = "text")
    private String commentContent;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn(name = "userId")
    private Users users;
    @ManyToOne (fetch =  FetchType.EAGER)
    @JoinColumn(name = "productId")
    private Product product;
    private LocalDateTime createDate;
}
