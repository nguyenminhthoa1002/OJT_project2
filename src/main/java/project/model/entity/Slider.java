package project.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "slider")
public class Slider extends BaseEntity{
    @Column(columnDefinition = "text")
    private String sliderDescription;
    @Column(columnDefinition = "text")
    private String sliderBackground;
}
