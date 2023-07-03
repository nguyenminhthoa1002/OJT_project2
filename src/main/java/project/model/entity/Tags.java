package project.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tags")
public class Tags extends BaseEntity {
}
