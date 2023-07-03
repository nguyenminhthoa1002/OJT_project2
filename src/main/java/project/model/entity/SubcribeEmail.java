package project.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "subcribeEmail")
public class SubcribeEmail extends BaseEntity{
    @Column(columnDefinition = "text")
    private String subcribeEmail;
}
